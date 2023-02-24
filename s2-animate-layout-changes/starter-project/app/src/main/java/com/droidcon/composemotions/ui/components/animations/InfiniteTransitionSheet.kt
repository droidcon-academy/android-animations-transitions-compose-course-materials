package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R
import com.droidcon.composemotions.ui.theme.ComposeMotionsTheme

const val COLOR_ANIMATION_DURATION = 2000

const val TEXT_MOVE_DURATION = 10000

/**
 * Demo for infinite animations in Jetpack Compose
 * This demo shows implementation of two infinite animations. One for the background and the other
 * for the offset of a text that moves across the screen. Note the custom type converter used for animating
 * the text offset. That's because Compose does not support Offset animation natively, so a custom
 * type converter has been used to convert the offset values to an [AnimationVector2D] type that the
 * animation system understands.
 *
 */
@Composable
fun InfiniteTransitionSheet() {
    //Define the infinite animation
    //TODO: Define infinite transition and name it "infinite"

    //Define animating background color
    //TODO: Define infinite background color "bgColor" using "infinite" from Material secondaryContainer to tertiary

    //Get the width of the screen
    val width = LocalConfiguration.current.screenWidthDp.toFloat()

    //Offset for the text
    //TODO: Make infinite offset, name it animatingOffset, animating from "width" to "-2*width"



    Box(
        Modifier
            .fillMaxSize()
            .drawBehind{
                //TODO: Change color to bgColor
                drawRect(Color.Gray)
            }

    ){
        Text(text = stringResource(R.string.please_take_care_of_your_code_by_documenting_it),
            modifier = Modifier
                .width(1000.dp)
                    //TODO: Change to use "animatingOffset"
                .offset{ IntOffset(100, 100)}
                ,
            maxLines = 1, //We want only one line that moves horizontally
            overflow = TextOverflow.Visible, //No clipping or ellipsis
            softWrap = false //Make the line long to have a marquee
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InfiniteTransitionSheetPreview() {
    ComposeMotionsTheme {
        InfiniteTransitionSheet()
    }
}