package com.droidcon.composemotions.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R
import com.droidcon.composemotions.ui.Screen

/**
 * Start screen for our flowchart of Jetpack Compose animations
 * @param modifier Modifier to change behavior
 * @param onNext Decide where to go next
 *
 */
@Composable
fun Start(
    modifier: Modifier = Modifier,
    onNext: (String) -> Unit
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .border(1.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.medium)
        ) {
            Icon(
                imageVector = Icons.Filled.Animation,
                contentDescription = Icons.Outlined.Animation.name,
                modifier = Modifier
                    .aspectRatio(1.1f)
                    .padding(64.dp)
                    .rotate(45f)
                    ,
                tint = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = stringResource(R.string.jetpack_compose_animations),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    onNext(Screen.AnimateLayoutContentDecision.route)
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    stringResource(R.string.start),
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }

}

@Preview(name = "Light", showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun StartPreview() {
    Start(onNext = {})
}