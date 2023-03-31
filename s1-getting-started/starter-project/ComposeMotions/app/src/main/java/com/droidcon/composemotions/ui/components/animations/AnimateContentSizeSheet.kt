package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R
import com.droidcon.composemotions.ui.theme.ComposeMotionsTheme

/**
 * Demo animation for Jetpack Compose's animateContentSize API
 * The text displayed in this composable has two modes: expanded and shrunk.
 * These two modes differ in the value of the maxLines parameter passed to the [Text] composable.
 * Then, an [animateContentSize] modifier has been applied to the text to animate the size change
 * between these two modes caused by different number of lines displayed in each mode.
 * Note the animationSpec parameter used to customize the animation of [animateContentSize]*
 */
@Composable
fun AnimateContentSizeSheet() {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.long_text), maxLines = if (expanded) 10 else 2,
            style = MaterialTheme.typography.bodyLarge, overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
                .align(Alignment.Start)
            //TODO: Add animateContentSize here

        )
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(if (expanded) R.string.show_less else R.string.show_more))
        }
    }
}

@Preview
@Composable
fun AnimateContentSizeSheetPreview() {
    ComposeMotionsTheme {
        AnimateContentSizeSheet()
    }
}