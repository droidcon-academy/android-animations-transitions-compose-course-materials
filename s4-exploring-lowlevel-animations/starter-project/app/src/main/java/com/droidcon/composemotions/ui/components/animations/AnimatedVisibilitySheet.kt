package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Demo for AnimatedVisibility animation
 */
@Composable
fun AnimatedVisibilitySheet() {
    val (checked, onCheckedChange) = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AnimatedVisibility(visible = checked,
            enter = fadeIn() + slideInVertically { height -> height },
            exit = fadeOut() + slideOutVertically { height -> - height }
        ) {
            Icon(
                imageVector = Icons.Outlined.Man, contentDescription = Icons.Outlined.Man.name,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
        Checkbox(
            checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
                .scale(2f)
        )


    }
}


@Preview(showBackground = true)
@Composable
fun AnimatedVisibilitySheetPreview() {
    AnimatedVisibilitySheet()
}