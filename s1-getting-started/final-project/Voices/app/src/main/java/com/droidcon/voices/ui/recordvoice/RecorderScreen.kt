@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package com.droidcon.voices.ui.recordvoice

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.droidcon.voices.R
import com.droidcon.voices.ui.VoiceDestinations
import com.droidcon.voices.ui.theme.VoicesAppTheme
import com.droidcon.voices.ui.util.Util
import kotlinx.coroutines.launch
import java.io.File


/**
 * Tag used for logging
 */
internal const val TAG = "RecordVoiceScreen"
@Composable
fun RecorderScreen(
    modifier: Modifier = Modifier,
    viewModel: RecorderViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /**
     * Snackbar Host State for displaying snackbar messages
     */
    val snackbarHostState = remember{SnackbarHostState()}

    val recorder = remember(Unit){
        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context) else MediaRecorder())
    }
    uiState.userMessage?.let { message->
        val messageText = stringResource(message)
        LaunchedEffect(snackbarHostState, viewModel, message, messageText) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.snackbarMessageShown()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            //Create the constraint references
            val (title, animationIndicator, controls) = createRefs()
            //Create guideline to constrain the recorder controls to
            val bottomGuideline = createGuidelineFromBottom(0.05f)


            /**
             * Guideline to use to constraint the Lottie recorder animation
             */
            val topGuideline = createGuidelineFromTop(0.15f)

            /**
             * Lottie composition that loads the Lottie file and prepares the animation
             */
            val lottieComposition = rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.recording))

            /**
             * Infinite transition to provide progress values to the lottie animation
             */
            val infiniteTransition = rememberInfiniteTransition()

            /**
             * Infinite animating value to be fed to the Lottie animation
             */
            val recordProgress by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000),
                    repeatMode = RepeatMode.Reverse
                )
            )

            //title
            Text(
                text =
                when (uiState.currentState) {
                    RecorderState.RECORDING -> stringResource(R.string.recording)
                    RecorderState.PAUSED -> stringResource(R.string.paused)
                    else -> stringResource(R.string.tap_mic_to_record)
                },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    }
                ,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium

            )

            //The Lottie animation (wrapped in AnimatedVisibility to make appearance/disappearance of the animation smooth)
            AnimatedVisibility (visible = uiState.currentState == RecorderState.RECORDING,
            enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(animationIndicator){
                        top.linkTo(title.bottom)
                        bottom.linkTo(controls.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                LottieAnimation(composition = lottieComposition.value, progress = {
                    recordProgress
                }
                )
            }

            //Recorder controls
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .requiredHeight(200.dp)
                    .constrainAs(controls) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(bottomGuideline)
                    }
                ,
                verticalAlignment = Alignment.CenterVertically

            ) {
                //Pause button
                IconButton(
                    enabled = uiState.currentState == RecorderState.RECORDING || uiState.currentState == RecorderState.PAUSED,
                    onClick = {
                        if (uiState.currentState == RecorderState.RECORDING) {
                            viewModel.updateRecorderState(RecorderState.PAUSED)
                        } else {
                            viewModel.updateRecorderState(RecorderState.RECORDING)
                        }
                    },
                    modifier = Modifier
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.onSurface,
                            MaterialTheme.shapes.small
                        )
                        .weight(1f)
                        .requiredSize(100.dp)
                        .fillMaxSize(0.7f)
                ) {
                    Icon(
                        Icons.Filled.Pause,
                        null,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    )
                }

                //Mic button
                IconButton(
                    enabled = uiState.currentState == RecorderState.IDLE,
                    onClick = {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            onNavigate(VoiceDestinations.RECORD_AUDIO_PERMISSION_ROUTE)
                        } else {
                            try {
                                recorder.apply {
                                    //Each time the record button is pressed, we reset and reconfigure the recorder
                                    reset()
                                    setAudioSource(MediaRecorder.AudioSource.MIC)
                                    setOutputFormat(Util.getOutputFormat(uiState.filenameExtension))
                                    setAudioEncoder(Util.getAudioEncoder(uiState.filenameExtension))
                                    val fileExtension =
                                        Util.getVoiceFileExtension(uiState.filenameExtension)
                                    setOutputFile("${context.filesDir}/${Util.TEMP_FILENAME}.$fileExtension")
                                }
                                recorder.prepare()
                                recorder.start()
                                viewModel.updateRecorderState(RecorderState.RECORDING)
                            } catch (e: Exception) {
                                Log.e(TAG, "Mic button: ")
                            }
                        }
                    },
                    modifier = Modifier
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.onSurface,
                            MaterialTheme.shapes.small
                        )
                        .weight(1f)
                        .requiredSize(100.dp)
                        .fillMaxSize(0.7f)
                ) {
                    Icon(
                        Icons.Filled.Mic,
                        null,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .scale(
                                when (uiState.currentState) {
                                    RecorderState.RECORDING -> 0.9f
                                    else -> 1f
                                }
                            )
                    )
                }

                //Stop button
                IconButton(
                    enabled = uiState.currentState != RecorderState.IDLE,
                    onClick = {
                        try {
                            recorder.stop()
                            val ext = uiState.filenameExtension.name.lowercase()
                            val fileName =
                                "${Util.generateFileName(uiState.filenamePrefix)}.$ext"
                            val metadataRetriever = MediaMetadataRetriever()

                            scope.launch {
                                viewModel.setIsLoading(true)
                                val pathPrefix = "${context.filesDir}"
                                if (Util.copyFile(
                                        context = context,
                                        src = "$pathPrefix/${Util.TEMP_FILENAME}.$ext",
                                        dst = "$pathPrefix/$fileName"
                                    )
                                ) {
                                    val file = File("$pathPrefix/$fileName")
                                    try {
                                        metadataRetriever.setDataSource(file.path)
                                    } catch (e: IllegalArgumentException) {
                                        Log.e(
                                            TAG,
                                            "RecorderScreen: Failed to call setDataSource: ${e.message}",
                                        )
                                    }
                                    val durationStr =
                                        metadataRetriever.extractMetadata(METADATA_KEY_DURATION)
                                            ?: "0"
                                    val duration = durationStr.toLong()
                                    viewModel.saveNewVoice(
                                        pathPrefix = context.filesDir.path,
                                        fileName,
                                        file.length(),
                                        duration
                                    )
                                    viewModel.showSnackbarMessage(R.string.voice_saved_successfully)
                                } else {
                                    viewModel.showSnackbarMessage(R.string.failed_to_save_voice)
                                }
                                viewModel.setIsLoading(false)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Stop button: ${e.message}")
                            viewModel.showSnackbarMessage(R.string.error_stopping_recorder)
                        }
                        viewModel.updateRecorderState(RecorderState.IDLE)
                    },
                    modifier = Modifier
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.onSurface,
                            MaterialTheme.shapes.small
                        )
                        .weight(1f)
                        .requiredSize(100.dp)
                        .fillMaxSize(0.7f)
                ) {
                    Icon(
                        Icons.Filled.Stop,
                        null,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                    )
                }


            }
        }
    }

}

@Preview
@Composable
fun VoiceRecorderPreview() {
    VoicesAppTheme {
        RecorderScreen {}
    }
}