package com.droidcon.voices.ui.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Surface(modifier = modifier) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.the_app_needs_your_permission_to_record_voice),
                modifier = Modifier
                    .padding(16.dp)

            )

            Button(onClick = {
                onRequestMicPermission()
            }) {
                Text(stringResource(id = R.string.okay))
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