package com.droidcon.composemotions.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Header layout used at the top of animation screens
 * @param text Text to display in header
 * @param modifier Modifier to apply to the layout
 */
@Composable
fun Header(text: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Text(
            text = text, style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                .padding(16.dp)
            , textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun HeaderPreview() {
    Header("rememberInfiniteTransition")
}