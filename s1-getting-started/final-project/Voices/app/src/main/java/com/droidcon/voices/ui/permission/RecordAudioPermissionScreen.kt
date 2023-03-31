@file:OptIn(ExperimentalAnimationApi::class)

package com.droidcon.voices.ui.permission

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.voices.R
import com.droidcon.voices.ui.theme.VoicesAppTheme

/**
 * Screen displayed to the user before opening system's audio permission request
 */
@Composable
fun RecordAudioPermissionScreen(
    modifier: Modifier = Modifier,
    onRequestMicPermission: () -> Unit
) {
    val show = remember { MutableTransitionState(false) }
    show.targetState = true

    //Animate appearance and disappearance of the Permissions screen content
    AnimatedVisibility ( visibleState = show,
        modifier = Modifier
        ,
        enter = fadeIn(tween(1_000)) + scaleIn(tween(1000)),
        exit = fadeOut() + scaleOut(),
        label = "Show Permission UI"

    ) {
        Surface(modifier = modifier
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp)

                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.the_app_needs_your_permission_to_record_voice),
                    modifier = Modifier
                        .padding(16.dp)

                )

                Button(onClick = {
                    show.targetState = false
                    onRequestMicPermission()
                }) {
                    Text(stringResource(id = R.string.okay))
                }
            }
        }
    }
}

@Preview
@Composable
fun MicPermissionScreenPreview() {
    VoicesAppTheme {
        RecordAudioPermissionScreen {
        }
    }
}