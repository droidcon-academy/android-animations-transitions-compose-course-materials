package com.droidcon.voices.ui.recordvoice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.voices.data.VoicesRepository
import com.droidcon.voices.data.local.database.Voice
import com.droidcon.voices.data.preference.DEFAULT_FILENAME_PREFIX
import com.droidcon.voices.data.preference.FilenameExtension
import com.droidcon.voices.data.preference.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Enum class representing different states of the audio player
 */
enum class RecorderState { IDLE, RECORDING, PAUSED }

data class RecorderUiState(
    val currentState: RecorderState,
    val isLoading: Boolean = false,
    val filenameExtension: FilenameExtension = FilenameExtension.MP3,
    val filenamePrefix : String = DEFAULT_FILENAME_PREFIX,
    val isVoiceSaved: Boolean = false,
    val userMessage : Int? = null
)

@HiltViewModel
class RecorderViewModel @Inject constructor(
    private val voicesRepository: VoicesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _preferences = userPreferencesRepository.userPrefs


    private val _currentState: MutableStateFlow<RecorderState> =
        MutableStateFlow(RecorderState.IDLE)

    //UI State is collecting individual states into one state flow
    val uiState: StateFlow<RecorderUiState> = combine(
        _isLoading, _currentState, _preferences, _userMessage
    ) { isLoading, currentState, preferences, userMessage ->
        RecorderUiState(
            currentState = currentState,
            isLoading = isLoading,
            filenamePrefix = preferences.filenamePrefix,
            filenameExtension = preferences.filenameExtension,
            userMessage = userMessage
            )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecorderUiState(currentState = RecorderState.IDLE)
        )


    /**
     * Clears the ViewModel's internal message state to dismiss the message from UI
     */
    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    /**
     * Shows snackbar message by setting the value of the ViewModel's message state to [message]
     */
    fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    /**
     * Updates the recorder state to the given [newState]
     */
    fun updateRecorderState(newState: RecorderState) {
        _currentState.value = newState
    }

    /**
     * Save a new voice with the given parameters
     * @param pathPrefix Prefix for the file
     * @param fileName File name of the voice file
     * @param size File size in bytes
     * @param duration File duration in seconds
     */
    fun saveNewVoice(pathPrefix: String, fileName: String, size : Long, duration : Long) {
        viewModelScope.launch{
            val voice = Voice(fileName = fileName, path = "$pathPrefix/$fileName", fileSize = size, duration = duration)
            voicesRepository.addVoice(voice)
        }
    }

    /**
     * Sets a boolean state to show whether an async operation is underway
     */
    fun setIsLoading(isLoading: Boolean){
        _isLoading.value = isLoading
    }
}