package com.droidcon.voices.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.droidcon.voices.R
import com.droidcon.voices.ui.VoiceDestinationArgs.TITLE_ARG
import com.droidcon.voices.ui.VoiceDestinationArgs.USER_MESSAGE_ARG
import com.droidcon.voices.ui.VoiceDestinationArgs.VOICE_ID_ARG
import com.droidcon.voices.ui.VoicesScreens.EDIT_VOICE_SCREEN
import com.droidcon.voices.ui.VoicesScreens.FAVORITES_SCREEN
import com.droidcon.voices.ui.VoicesScreens.RECORD_AUDIO_PERMISSION_SCREEN
import com.droidcon.voices.ui.VoicesScreens.RECORD_SCREEN
import com.droidcon.voices.ui.VoicesScreens.SETTINGS_SCREEN
import com.droidcon.voices.ui.VoicesScreens.VOICES_SCREEN
import com.droidcon.voices.ui.VoicesScreens.VOICE_DETAIL_SCREEN


/**
 * Sealed class used to specify bottom navigation items
 */
sealed class BottomNavDestination(@StringRes val name: Int, val icon: ImageVector, val route: String){
    object VoicesDestination : BottomNavDestination(R.string.voices, Icons.Default.LibraryMusic, VOICES_SCREEN)
    object RecordDestination: BottomNavDestination(R.string.record, Icons.Default.Mic, RECORD_SCREEN)
    object FavoritesDestination: BottomNavDestination(R.string.favorites, Icons.Default.Bookmarks, FAVORITES_SCREEN)
    object SettingsDestination: BottomNavDestination(R.string.settings, Icons.Default.Settings, SETTINGS_SCREEN)
}

/**
 * This class contains constants representing different screens in our app
 */
private object VoicesScreens {
    const val VOICES_SCREEN = "voicesScreen"
    const val RECORD_SCREEN = "recordScreen"
    const val EDIT_VOICE_SCREEN = "editVoiceScreen"
    const val VOICE_DETAIL_SCREEN = "voiceDetailScreen"
    const val FAVORITES_SCREEN = "favoritesScreen"
    const val SETTINGS_SCREEN = "settingsScreen"
    const val RECORD_AUDIO_PERMISSION_SCREEN = "recordAudioPermissionScreen"
}

/**
 * Constants used as argument names to pass information to navigation destinations
 */
object VoiceDestinationArgs{
    const val USER_MESSAGE_ARG = "userMessage"
    const val VOICE_ID_ARG = "voiceId"
    const val TITLE_ARG = "title"
}

/**
 * Constants representing navigation routes leading to different destinations
 */
object VoiceDestinations {
    const val SETTINGS_ROUTE = SETTINGS_SCREEN
    const val FAVORITES_ROUTE = FAVORITES_SCREEN
    const val VOICES_ROUTE = VOICES_SCREEN
    const val RECORD_ROUTE = RECORD_SCREEN
    const val EDIT_ROUTE = "$EDIT_VOICE_SCREEN?/{$TITLE_ARG}/{$VOICE_ID_ARG}"
    const val RECORD_AUDIO_PERMISSION_ROUTE = RECORD_AUDIO_PERMISSION_SCREEN
}

/**
 * This class contains ready actions to facilitate navigation to different destinations
 */
class VoiceNavigationActions(private val navController: NavHostController){
    fun navigateToVoices(userMessage: Int = 0){
        navController.navigate(
            VOICES_SCREEN.let{
                if (userMessage != 0) "$USER_MESSAGE_ARG=$userMessage" else it
            }
        ){
            launchSingleTop = true
            popUpTo(navController.graph.startDestinationId){
                inclusive = false
                saveState = true
            }
            restoreState = false
        }

    }

    fun navigateToVoiceDetail(voiceId: String) {
        navController.navigate("$VOICE_DETAIL_SCREEN/$voiceId")
    }

    fun navigateToEditVoice(title: String, voiceId: String){
        navController.navigate(
            "$EDIT_VOICE_SCREEN/$title/$voiceId"
        )
    }

    fun navigateToRecordVoice() {
        navController.navigate(VoiceDestinations.RECORD_ROUTE){
            launchSingleTop = true
            popUpTo(navController.graph.startDestinationId){
                inclusive = false
                saveState = true
            }
        }
    }

    fun navigateToFavoritesScreen() {
        navController.navigate(VoiceDestinations.FAVORITES_ROUTE){
            launchSingleTop = true
            popUpTo(navController.graph.startDestinationId){
                inclusive = false
                saveState = true
            }
        }
    }

    fun navigateToSettingsScreen() {
        navController.navigate(VoiceDestinations.SETTINGS_ROUTE){
            launchSingleTop = true
            popUpTo(navController.graph.startDestinationId){
                inclusive = false
                saveState = true
            }
        }
    }
}