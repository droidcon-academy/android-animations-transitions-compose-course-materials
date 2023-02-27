package com.droidcon.composemotions.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A Container for an animation demo
 * @param modifier Optional modifier passed to change behavior of the composable
 * @param content Content to be displayed inside the container
 */
@Composable
fun AnimationContainer(modifier : Modifier = Modifier,
                       content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier = modifier) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(1.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.shapes.large)
        ) {
            content()
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AnimationContainerPreview() {
    AnimationContainer(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(Icons.Outlined.ThumbUp, contentDescription = Icons.Outlined.ThumbUp.name,
            modifier = Modifier
            .fillMaxSize()
        )
    }
}