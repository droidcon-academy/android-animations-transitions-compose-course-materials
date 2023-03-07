package com.droidcon.voices.data

import kotlinx.coroutines.flow.Flow
import com.droidcon.voices.data.local.database.Voice
import com.droidcon.voices.data.local.database.VoiceDao
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Voices repository used by the data layer
 */
interface VoicesRepository {
    val voices: Flow<List<Voice>>

    fun getVoicesStream(): Flow<Result<List<Voice>>>

    suspend fun addVoice(voice: Voice)
    suspend fun updateVoice(voice: Voice)

}

/**
 * Default implementation of the voices repository used by Hilt
 */
class DefaultVoicesRepository @Inject constructor(
    private val voiceDao: VoiceDao
) : VoicesRepository {

    override val voices: Flow<List<Voice>> =
        voiceDao.getVoices()

    override suspend fun addVoice(voice: Voice) {
        coroutineScope {
            launch {
                voiceDao.insertVoice(voice)
            }
        }
    }

    override suspend fun updateVoice(voice: Voice) {
        coroutineScope {
            voiceDao.updateVoice(voice)
        }
    }

    override fun getVoicesStream(): Flow<Result<List<Voice>>> {
        return voiceDao.observeVoices().map{
            Result.Success(it)
        }
    }
}
