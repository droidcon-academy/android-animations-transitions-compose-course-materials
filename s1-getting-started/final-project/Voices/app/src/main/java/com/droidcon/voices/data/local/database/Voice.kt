package com.droidcon.voices.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.UUID

/**
 * @param fileName Name of the voice file
 * @param path Path of the voice file
 * @param date Date of the voice file in seconds
 * @param isFavorite Whether the voice is marked as favorite
 * @param duration duration of the voice file
 * @param fileSize Size of the voice file in bytes
 * @param id Unique identifier of the voice file
 *
 */
@Entity
data class Voice constructor(
    @ColumnInfo(name = "filename") var fileName: String,
    @ColumnInfo(name = "path") var path: String,
    @ColumnInfo(name = "date") var date: Long = Calendar.getInstance().time.time / 1_000_000,
    @ColumnInfo(name = "isfavorite") var isFavorite: Boolean = false,
    @ColumnInfo(name = "duration") var duration: Long = 0,
    @ColumnInfo(name = "filesize") var fileSize : Long = 0,
    @PrimaryKey @ColumnInfo(name = "voiceid")
        var id: String = UUID.randomUUID().toString()
)


/**
 * Dao interface for the [androidx.room.Room] database
 */
@Dao
interface VoiceDao {
    @Query("SELECT * FROM voice ORDER BY voiceid DESC LIMIT 10")
    fun getVoices(): Flow<List<Voice>>

    /**
     * Add new [Voice] to the database
     */
    @Insert
    suspend fun insertVoice(item: Voice)

    /**
     * Observes list of voices.
     *
     * @return all voices.
     */
    @Query("SELECT * FROM voice ORDER BY date DESC LIMIT 10")
    fun observeVoices(): Flow<List<Voice>>

    /**
     * Update one or more existing [Voice] items in the database
     */
    @Update
    suspend fun updateVoice(vararg voice: Voice)

    @Delete
    suspend fun deleteVoice(vararg voice: Voice)
}
