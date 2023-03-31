package com.droidcon.voices.ui.voices

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.voices.R
import com.droidcon.voices.data.Result
import com.droidcon.voices.data.Result.Success
import com.droidcon.voices.data.VoicesRepository
import com.droidcon.voices.data.local.database.Voice
import com.droidcon.voices.data.preference.SortBy
import com.droidcon.voices.data.preference.SortOrder
import com.droidcon.voices.ui.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * key used to save the current filtering in SavedStateHandle
 */
const val VOICES_SORT_ORDER_KEY = "VOICES_SORT_ORDER_SAVED_STATE_KEY"

/**
 * Saved state key for sorting voices by different factors
 */
const val VOICES_SORT_BY_KEY = "VOICES_SORT_BY_SAVED_STATE_KEY"

/**
 * Saved state key for filtering voices
 */
const val VOICES_FILTER_KEY = "VOICES_FILTER_KEY"
data class VoicesUiState (
//    object Loading : VoiceUiState
//    data class Error(val throwable: Throwable) : VoiceUiState
//    data class Success(val data: List<Voice>) : VoiceUiState
    val items: List<Voice> = emptyList(),
    val userMessage: Int? = null,
    val isLoading: Boolean = false,
    val activeVoiceState: ActiveVoiceState = ActiveVoiceState.IDLE,
    val activeVoiceId: String? = null,
    val sortOrder: SortOrder = SortOrder.ASCENDING,
    val sortBy : SortBy = SortBy.DEFAULT
)

/**
 * Enum class used to keep track of currently active voice item
 */
enum class ActiveVoiceState { Playing, Paused, IDLE }

/**
 * Holds information about the current active voice being played or otherwise operated on
 */
data class ActiveVoiceUiState (
    val activeVoiceId: String? = null,
    val activeVoiceState: ActiveVoiceState = ActiveVoiceState.IDLE
)

/**
 * Holds information about sorting items (i.e. voices)
 */
data class SortInfo (
    val sortOrder : SortOrder = SortOrder.ASCENDING,
    val sortBy : SortBy = SortBy.DEFAULT
)


@HiltViewModel
class VoicesViewModel @Inject constructor(
    private val voicesRepository: VoicesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val _isLoading = MutableStateFlow(false)

    private val _activeVoiceUiState = MutableStateFlow(ActiveVoiceUiState())


    private val _savedSortInfo = combine(
        savedStateHandle.getStateFlow(VOICES_SORT_ORDER_KEY, SortOrder.ASCENDING),
        savedStateHandle.getStateFlow(VOICES_SORT_BY_KEY, SortBy.DEFAULT)
    ){sortOrder, sortBy->
        SortInfo(
            sortOrder = sortOrder,
            sortBy = sortBy
        )
    }

    private val _filterType = savedStateHandle.getStateFlow(
        VOICES_FILTER_KEY, VoicesFilterType.ALL_VOICES
    )

    private val _voicesAsync = combine(voicesRepository.getVoicesStream(), _filterType, _savedSortInfo){voices, type, sortInfo ->
        filterVoices(voices, type, sortInfo)
    }
    .map { Async.Success(it) }
    .onStart<Async<List<Voice>>> {emit(Async.Loading)}

    fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }

    val uiState  = combine(
        _userMessage, _isLoading, _activeVoiceUiState, _voicesAsync, _savedSortInfo
    ) { userMessage, isLoading, activeVoiceUiState, voicesAsync, savedSortInfo  ->
        when (voicesAsync) {
            Async.Loading -> {
                VoicesUiState(isLoading = true)
            }
            is Async.Success -> {
                VoicesUiState(
                    items = voicesAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage,
                    activeVoiceState = activeVoiceUiState.activeVoiceState,
                    activeVoiceId = activeVoiceUiState.activeVoiceId,
                    sortOrder = savedSortInfo.sortOrder,
                    sortBy = savedSortInfo.sortBy
                )
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VoicesUiState()
    )


    fun updateActiveVoice(id: String) {
        _activeVoiceUiState.value = _activeVoiceUiState.value.copy(activeVoiceId = id)
    }

    fun updateActiveVoiceState(nextVoiceState: ActiveVoiceState) {
        _activeVoiceUiState.value = _activeVoiceUiState.value.copy(activeVoiceState = nextVoiceState)
    }

    /**
     * Filter voices with the given [filterType]
     * @param voicesResult list of voices to filter
     * @param filterType Type of filter used
     * @param sortInfo Sort information used for sorting the filtered items
     *
     * @return List<Voice>
     */
    private fun filterVoices(
        voicesResult: Result<List<Voice>>,
        filterType: VoicesFilterType,
        sortInfo: SortInfo
    ): List<Voice> = if (voicesResult is Success) {
        val filteredItems = filterItems(voicesResult.data, filterType)
        sortVoices(filteredItems, sortInfo)
    } else {
        showSnackbarMessage(R.string.error_loading_voices)
        emptyList()
    }

    /**
     * Sorts voices based on the requested criteria
     * @param voices Unsorted voices
     * @param sortInfo Sort criteria (i.e. sort-by and sort-order)
     * @return Sorted list of voices
     */
    private fun sortVoices(voices: List<Voice>, sortInfo: SortInfo): List<Voice> {
        return if (sortInfo.sortOrder == SortOrder.ASCENDING){
            when(sortInfo.sortBy){
                SortBy.DEFAULT -> voices
                SortBy.SIZE -> voices.sortedBy { it.fileSize }
                SortBy.NAME -> voices.sortedBy { it.fileName }
                SortBy.DATE -> voices.sortedBy { it.date }
                SortBy.DURATION -> voices.sortedBy { it.duration }
            }
        }
        else {
            return when(sortInfo.sortBy){
                SortBy.DEFAULT -> voices
                SortBy.SIZE -> voices.sortedByDescending { it.fileSize }
                SortBy.NAME -> voices.sortedByDescending { it.fileName }
                SortBy.DATE -> voices.sortedByDescending { it.date }
                SortBy.DURATION -> voices.sortedByDescending { it.duration }
            }
        }
    }

    private fun filterItems(voices: List<Voice>, filterType: VoicesFilterType): List<Voice> {
        val voicesToShow = ArrayList<Voice>()
        // We filter the tasks based on the requestType
        for (voice in voices) {
            when (filterType) {
                VoicesFilterType.ALL_VOICES -> voicesToShow.add(voice)
                VoicesFilterType.FAVORITE_VOICES -> {
                    if(voice.isFavorite){
                        voicesToShow.add(voice)
                    }
                }
            }
        }
        return voicesToShow
    }

    /**
     * Updates the value of sort-order state in [savedStateHandle]
     */
    fun setSortOrder(sortOrder: SortOrder){
        savedStateHandle[VOICES_SORT_ORDER_KEY] = sortOrder
    }

    /**
     * Updates the value of sort-by (sort-type) in [savedStateHandle]
     */
    fun setSortBy(sortBy: SortBy) {
        savedStateHandle[VOICES_SORT_BY_KEY] = sortBy
    }

    /**
     * Update filter type for the voice list
     */
    fun setFilterType(filterType: VoicesFilterType){
        savedStateHandle[VOICES_FILTER_KEY] = filterType
    }

    fun toggleBookmark(voice: Voice) {
        viewModelScope.launch {
            voicesRepository.updateVoice(voice.copy(isFavorite = !voice.isFavorite))
        }
    }

    fun deleteVoice(vararg voice: Voice) {
        viewModelScope.launch {
            voicesRepository.deleteVoice(*voice)
        }
    }

    fun snackbarMessageShown(){
        _userMessage.value = null
    }
}

