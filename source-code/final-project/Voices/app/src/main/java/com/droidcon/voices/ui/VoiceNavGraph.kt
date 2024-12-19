@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package com.droidcon.voices.ui

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.rememberNavController
import com.droidcon.voices.ui.VoiceDestinations.VOICES_ROUTE
import com.droidcon.voices.ui.permission.RecordAudioPermissionScreen
import com.droidcon.voices.ui.recordvoice.RecorderScreen
import com.droidcon.voices.ui.settings.SettingsScreen
import com.droidcon.voices.ui.voices.VoicesScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * Slide duration for navigation transitions
 */
internal const val DEFAULT_ANIM_DURATION = 1_500

/**
 * The navigation graph for the app
 */
@Composable
fun MainNavigation() {
    val navController = rememberAnimatedNavController()
    val navigationActions by remember(navController){ derivedStateOf { VoiceNavigationActions(navController)}}
    val snackbarHostState = remember{SnackbarHostState()}
    val micPermissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {ok->
        if(ok){
           navController.navigate(VoiceDestinations.RECORD_ROUTE){
               popUpTo(VoiceDestinations.RECORD_ROUTE){
                   saveState = true
               }
           }
        }
    })
    Scaffold(topBar = {},
        bottomBar = { MyBottomBar(navController = navController, onNavigate = {
            when(it){
                VoiceDestinations.RECORD_ROUTE -> navigationActions.navigateToRecordVoice()
                VoiceDestinations.VOICES_ROUTE -> navigationActions.navigateToVoices()
                VoiceDestinations.SETTINGS_ROUTE -> navigationActions.navigateToSettingsScreen()
            }
        })
        },
        snackbarHost = {SnackbarHost(snackbarHostState)}
    ) {paddingValues->
        val context = LocalContext.current
        AnimatedNavHost(navController = navController, startDestination = VoiceDestinations.RECORD_ROUTE, modifier = Modifier.padding(paddingValues),
            enterTransition = {
                when(targetState.destination.route){
                    VoiceDestinations.VOICES_ROUTE -> fadeIn(animationSpec =
                    tween(
                        DEFAULT_ANIM_DURATION)
                    )
                    else -> slideInHorizontally(
                        animationSpec = tween(DEFAULT_ANIM_DURATION),
                        initialOffsetX = { width-> width }
                    )
                }
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(DEFAULT_ANIM_DURATION),
                    targetOffsetX = { width -> -width }
                )
            },
            popEnterTransition = {
                slideInVertically(
                    animationSpec = tween(DEFAULT_ANIM_DURATION),
                    initialOffsetY = { height -> height }
                )
            },
            popExitTransition = {
                slideOutVertically(
                    animationSpec = tween(DEFAULT_ANIM_DURATION),
                    targetOffsetY = { height-> -height }
                )
            }
            ) {
            composable(VoiceDestinations.RECORD_ROUTE) {
                if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    RecorderScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
                    ) { route ->
                        navController.navigate(route)
                    }
                }
                else {
                    RecordAudioPermissionScreen (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium),
                        onRequestMicPermission = { micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO) }
                    )
                }
            }
            composable(VoiceDestinations.VOICES_ROUTE){
                VoicesScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium))
            }
            composable(VoiceDestinations.RECORD_AUDIO_PERMISSION_ROUTE){
                RecordAudioPermissionScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium),
                    onRequestMicPermission = {
                    micPermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                })
            }
            composable(VoiceDestinations.SETTINGS_ROUTE,
                exitTransition = {
                    if (targetState.destination.route == VOICES_ROUTE) {
                        scaleOut(
                            animationSpec = tween(DEFAULT_ANIM_DURATION)
                        )
                    }
                    else null
                }
            ){
                SettingsScreen(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium))
            }
        }
    }
}

