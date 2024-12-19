package com.droidcon.voices.ui.settings

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.droidcon.voices.data.preference.FilenameExtension
import com.droidcon.voices.data.preference.UserPreferences
import com.droidcon.voices.data.preference.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
data class SettingsUiState(
    val filenamePrefix: String = UserPreferencesRepository.DEFAULT_FILENAME_PREFIX,
    val filenameExtension: FilenameExtension = FilenameExtension.valueOf(UserPreferencesRepository.DEFAULT_FILENAME_EXTENSION),
    val userMessage: Int? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _userMessage : MutableStateFlow<Int?> = MutableStateFlow(null)


    //Access to user preferences as a stream
    private val userPreferencesFlow = userPreferencesRepository.userPrefs

    val uiState = combine(
        userPreferencesFlow, _userMessage
    ) { userPreferences, userMessage ->
        SettingsUiState(
            filenamePrefix = userPreferences.filenamePrefix,
            filenameExtension = userPreferences.filenameExtension,
            userMessage = userMessage
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SettingsUiState()
    )

    fun updateFilenamePrefix(filenamePrefix: String) {
        viewModelScope.launch {
            userPreferencesRepository.updateFilenamePrefix(filenamePrefix)
        }
    }

    /**
     * Update the value of [_userMessage] with the parameter
     */
    fun showSnackbarMessage(@StringRes message: Int){
        _userMessage.value = message
    }

    /**
     * Set the value of [_userMessage] to null so that our UI learns not to display a snackbar message
     */
    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    /**
     * Update filename extension in the preferences
     */
    fun updateFilenameExtension(extension: String) {
        viewModelScope.launch{
            userPreferencesRepository.updateFilenameExtension(extension)
        }
    }


}