package com.droidcon.composemotions.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R

/**
 * Terminal (ending) screens of our Jetpack Compose animation flowchart, which also contains
 * an animation demo
 * @param modifier Modifier to apply to the layout
 * @param header Composable that defines the content of the header for this terminal block
 * @param content The main content of the layout
 */
@Composable
fun Terminal(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
        ) {
            Box(
                Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                header()
            }

            Box(Modifier
                .fillMaxSize()
                ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun TerminalPreview() {
    Terminal(
        header = {
            Text(
                text = "Title", modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Terminal Preview",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}