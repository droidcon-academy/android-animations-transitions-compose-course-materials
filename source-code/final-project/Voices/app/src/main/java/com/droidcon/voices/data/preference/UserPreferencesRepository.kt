package com.droidcon.voices.data.preference

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject

/**
 * This class represents different file extensions the voice recorder can support
 */
enum class FilenameExtension {
    MP3,
    M4A,
    AMR
}

/**
 * Default filename extension
 */
const val DEFAULT_FILENAME_EXTENSION = "MP3"

/**
 * Default filename prefix
 */
const val DEFAULT_FILENAME_PREFIX = "Voice"


data class UserPreferences(
    val filenamePrefix : String = DEFAULT_FILENAME_PREFIX,
    val filenameExtension : FilenameExtension = FilenameExtension.valueOf(DEFAULT_FILENAME_EXTENSION),
)

class UserPreferencesRepository @Inject constructor (
    private val dataStore : DataStore<Preferences>
) {

    companion object {
        private const val USER_PREFERENCES_NAME = "voices_app_preferences"
        const val DEFAULT_FILENAME_PREFIX = "Voice"
        const val DEFAULT_FILENAME_EXTENSION = "MP3"

        /**
         * Tag for logging
         */
        private const val TAG = "UserPreferencesRepo"
    }

    /**
     * Holds keys for accessing preferences
     */
    private object PreferencesKeys {
        val FILENAME_PREFIX = stringPreferencesKey("filename_prefix")
        val FILENAME_EXTENSION = stringPreferencesKey("filename_extension")
    }

    /**
     * Preferences flow for accessing preferences
     */
    val userPrefs: Flow<UserPreferences> = dataStore.data
        .catch {exception->             // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map{preferences->
            mapUserPreferences(preferences)
        }.stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences()
        )

    /**
     * Update filename prefix in preferences
     * @param filenamePrefix Voice file prefix to update to
     */
    suspend fun updateFilenamePrefix(filenamePrefix: String){
        dataStore.edit { preferences->
            preferences[PreferencesKeys.FILENAME_PREFIX] = filenamePrefix
        }
    }

    /**
     * Fetches the latest preferences from [preferences]
     * @param preferences [Preferences] instance to retrieve values from
     * @return [UserPreferences]
     */
    private fun mapUserPreferences(preferences: Preferences): UserPreferences {
        // Get filename prefix value, defaulting to empty string if not set:
        val filenamePrefix = preferences[PreferencesKeys.FILENAME_PREFIX] ?: DEFAULT_FILENAME_PREFIX
        val filenameExtension = preferences[PreferencesKeys.FILENAME_EXTENSION] ?: DEFAULT_FILENAME_EXTENSION

        Log.d(TAG, "mapUserPreferences: Prefix: $filenamePrefix, Extension: $filenameExtension")
        return UserPreferences(filenamePrefix, FilenameExtension.valueOf(filenameExtension))
    }

    /**
     * Update the filename extension settings
     * @param extension New extension to update to
     */
    suspend fun updateFilenameExtension(extension: String) {
        dataStore.edit {preferences->
            preferences[PreferencesKeys.FILENAME_EXTENSION] = extension
        }
    }
}