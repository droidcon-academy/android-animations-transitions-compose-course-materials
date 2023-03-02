package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R
import com.droidcon.composemotions.ui.theme.ComposeMotionsTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Animation demo using [Animatable] in Jetpack Compose
 * Animatable is a value holder that has been used to create high-level animations
 * like [animateFloatAsState], [animateIntAsState], [animateOffsetAsState], etc.
 * Because [Animatable] gives finer control over animation by providing methods like
 * [Animatable.snapTo], [Animatable.animateTo], and [Animatable.animateDecay], it can be used to implement advanced animations that
 * need to be synced with other states, for example fling animation.
 */
@Composable
fun AnimatableSheet() {
    /**
     *  Animatable holding offset values
     */
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    //TODO: Make offset Animatable

    Box(
        Modifier
            .fillMaxSize()
            //Detect taps on the screen and capture pressed locations
            .pointerInput(Unit) {
                //Launch a coroutine scope for handling pointer inputs from the user
                coroutineScope {
                    //A forever loop means tap gesture
                    while (true) {
                        awaitPointerEventScope {
                            //Detect the pressed position, whose type is Offset
                            val position = awaitFirstDown().position
                            //Update offset value to new position with an animation
                            offset = position
                            //TODO: Animate the offset to position
                        }

                    }
                }
            }
    ) {
        //Text to display current offset
        Text(text = stringResource(R.string.position_x_y, offset.x, offset.y),
            modifier = Modifier.align(Alignment.Center)
        )

        //Round box that will move around with user tap
        Box(
            Modifier
                //Using offset modifier here to move the item around the screen
                .offset {
                    IntOffset(
                        //IntOffset needs Int arguments
                        offset.x.roundToInt(),
                        offset.y.roundToInt()
                    )
                }
                .size(50.dp)
                //Make the box round
                .clip(CircleShape)
                .background(Color.Magenta, CircleShape)

        )
    }
}

@Preview
@Composable
fun AnimatableSheetPreview() {
    ComposeMotionsTheme {
        AnimatableSheet()
    }
}