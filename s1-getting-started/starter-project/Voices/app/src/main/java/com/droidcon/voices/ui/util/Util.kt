package com.droidcon.voices.ui.util

import android.content.Context
import android.content.Intent
import android.media.MediaCodec
import android.media.MediaRecorder
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.droidcon.voices.R
import com.droidcon.voices.data.preference.FilenameExtension
import com.droidcon.voices.ui.voices.ActiveVoiceState
import java.io.File
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Calendar.FRIDAY
import java.util.Calendar.MONDAY
import java.util.Calendar.SATURDAY
import java.util.Calendar.SUNDAY
import java.util.Calendar.THURSDAY
import java.util.Calendar.TUESDAY
import java.util.Calendar.WEDNESDAY


sealed class Util {
    companion object {
        /**
         * Default height for the media player
         */
        val PlayerViewHeight = 100.dp

        private const val FILE_PROVIDER_AUTHORITY = "com.droidcon.provider"

        const val TEMP_FILENAME = "temp"

        private const val TAG = "Util"

        private const val DEFAULT_FILENAME_PREFIX = "Voice"

        fun generateFileName(prefix: String): String {
            val cal = Calendar.getInstance()
            val sb = StringBuilder()
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val hour = normalizeTimeComponent(cal.get(Calendar.HOUR))
            val min = normalizeTimeComponent(cal.get(Calendar.MINUTE))
            val sec = normalizeTimeComponent(cal.get(Calendar.SECOND))
            sb.apply {
                append(getNameOfDayOfWeek(dayOfWeek))
                append("_")
                append(year)
                append(normalizeMonth(month))
                append(normalizeDay(day))
                append("_")
                append("$hour$min$sec")
            }
            return "$prefix $sb"
        }

        /**
         * Returns given month in a standard way. E.g. 0 becomes 01 because JANUARY is 0 in [Calendar]
         */
        private fun normalizeMonth(month: Int): String {
            val m = month + 1 //Add one because month index starts from 0
            return if (m.toString().length == 1)  "0$m" else "$m" // If month is one digit, prepend 0 to it
        }

        /**
         * Prepends 0 to single digit days to make the constructed date string more readable
         */
        private fun normalizeDay(day: Int): String {
            return if (day.toString().length == 1) "0$day" else "$day" // If day is one digit, prepend 0 to it
        }

        private fun normalizeTimeComponent(component: Int) : String {
            return if ("$component".length == 1) "0$component" else "$component"
        }

        suspend fun copyFile(context: Context, src: String, dst: String): Boolean {
            return try {
                val srcFile = File(src)
                val dstFile = File(dst)
                srcFile.copyTo(dstFile)
                true
            } catch (e: Exception) {
                Log.e(TAG, "copyFile: ${e.message}")
                false
            }
        }

        fun selectIconForState(voiceState: ActiveVoiceState): ImageVector {
            return when (voiceState) {
                ActiveVoiceState.IDLE -> Icons.Filled.PlayArrow
                ActiveVoiceState.Playing -> Icons.Filled.Pause
                else -> Icons.Filled.Pause
            }
        }

        fun selectTextForState(voiceState: ActiveVoiceState): Int {
            return when (voiceState) {
                ActiveVoiceState.IDLE -> R.string.idle
                ActiveVoiceState.Playing -> R.string.playing
                else -> R.string.paused
            }
        }

        fun selectNextStateOnTap(currentState: ActiveVoiceState): ActiveVoiceState {
            return when (currentState) {
                ActiveVoiceState.IDLE -> ActiveVoiceState.Playing
                ActiveVoiceState.Playing -> ActiveVoiceState.Paused
                ActiveVoiceState.Paused -> ActiveVoiceState.Playing
            }
        }

        /**
         * Shares a file specified by [path]
         * @param context Context used to create Intent
         * @param path Path to the voice file to be shared
         */
        fun shareFile(context: Context, path: String) {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                val uri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, File(path))
                shareIntent.setDataAndType(uri, context.contentResolver.getType(uri))
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                val chooser =
                    Intent.createChooser(shareIntent, context.getString(R.string.share_voice))
                context.startActivity(chooser)
            } catch (e: Exception) {
                Log.e(TAG, "share: ${e.message}")
            }
        }

        private fun getNameOfDayOfWeek(dayOfWeek: Int) : String {
            return when(dayOfWeek){
                SUNDAY -> "Sun"
                MONDAY -> "Mon"
                TUESDAY -> "Tue"
                WEDNESDAY -> "Wed"
                THURSDAY -> "Thu"
                FRIDAY -> "Fri"
                SATURDAY -> "Sat"
                else -> "Undefined"
            }
        }

        /**
         * Select appropriate output format for the given [filenameExtension]
         */
        fun getOutputFormat(filenameExtension: FilenameExtension): Int {
            return when(filenameExtension){
                FilenameExtension.MP3 -> MediaRecorder.OutputFormat.MPEG_4
                FilenameExtension.AMR -> MediaRecorder.OutputFormat.AMR_WB
                FilenameExtension.M4A -> MediaRecorder.OutputFormat.MPEG_4
            }
        }

        /**
         * Select appropriate audio encoder for the given [filenameExtension]
         */
        fun getAudioEncoder(filenameExtension: FilenameExtension): Int {
            return when(filenameExtension){
                FilenameExtension.MP3 -> MediaRecorder.AudioEncoder.AAC_ELD
                FilenameExtension.AMR -> MediaRecorder.AudioEncoder.AMR_WB
                FilenameExtension.M4A -> MediaRecorder.AudioEncoder.AAC
            }
        }

        /**
         * Select appropriate filename extension for the given [filenameExtension]
         */
        fun getVoiceFileExtension(filenameExtension: FilenameExtension) : String {
            return when(filenameExtension){
                FilenameExtension.MP3 -> "mp3"
                FilenameExtension.M4A -> "m4a"
                FilenameExtension.AMR -> "amr"
            }
        }

        /**
         * Converts given milliseconds value and converts it to time string in the form **XhXmXs**
         */
        fun msToTimeStr(ms : Long) : String {
            val seconds = ms / 1_000
            return if(seconds >= 3600) {
                (seconds / 3600).toString() + "h" + (seconds % 3600 / 60).toString() + "m" + (seconds % 3600 % 60).toString() + "s"
            } else if (seconds >= 60) {
                (seconds / 60).toString() + "m" + (seconds % 60).toString() + "s"
            } else {
                "${seconds}s"
            }

        }


    }


}