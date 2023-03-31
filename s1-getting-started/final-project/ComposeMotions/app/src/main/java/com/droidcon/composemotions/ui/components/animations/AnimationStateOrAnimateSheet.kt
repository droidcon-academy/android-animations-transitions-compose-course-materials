package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R

/**
 * Duration for tween animation
 */
internal const val ANIMATION_DURATION = 2000


/**
 *  Demo for Compose animations using *AnimationState* or *animate*
 */
@Composable
fun AnimationStateOrAnimateSheet() {
    Column(Modifier.fillMaxSize()) {
        /**
         * Translation state variable that will be mutated by the [animate] method
         */
        var translate by remember { mutableStateOf(50f) }

        /**
         * [AnimationState] variable to track the state of animation for [Color]
         */
        val animationState = remember {
            AnimationState(
                //Custom vector converter for Color
                typeConverter = TwoWayConverter(
                    //Convert animating type to vector
                    convertToVector = { AnimationVector4D(it.red, it.green, it.blue, it.alpha) },
                    //Convert vector to animating type
                    convertFromVector = { Color(it.v1, it.v2, it.v3, it.v4) }),
                initialValue = Color.Cyan
            )
        }

        //Animate background color using "AnimationState"
        LaunchedEffect(Unit) {
            //Takes initial color from AnimationState and animates to the specified color
            animationState.animateTo(
                targetValue = Color.Green,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = ANIMATION_DURATION),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }

        //Animate translate using "animate"
        //Unlike "AnimationState"", "animate" specifies both initial and target value for the animation
        LaunchedEffect(Unit) {
            animate(
                //Initial value of animation
                initialValue = 50f,
                //Target value of animation
                targetValue = 200f,
                //Use an infinite transition to animate from initialValue to targetValue
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = ANIMATION_DURATION),
                    repeatMode = RepeatMode.Reverse
                )
            ) { value, _ ->
                //Update translate with the latest animation value
                translate = value
            }
        }

        //Container for "AnimationState"
        //Here, the value of AnimationState will be used to set the background color
        Box(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f)
                .drawBehind{
                    drawRect(animationState.value)
                }
                .align(Alignment.CenterHorizontally)
        ) {
            //Title for "AnimationState"
            Text(
                stringResource(R.string.animate_using_animationstate), Modifier
                    .padding(8.dp)
                    .align(Alignment.TopStart)
            )

        }


        //Container for "animate"
        Box(
            Modifier
                .weight(1f)
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            //Title for "animate"
            Text(stringResource(R.string.animate_using_animate))

            //Bitmap to use with drawImage
            val imgBitmap = ImageBitmap.imageResource(R.drawable.cat1)

            //Canvas to draw image
            Canvas(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                //Draw the image with the ImageBitmap
                drawImage(
                    //ImageBitmap instance
                    image = imgBitmap,
                    //Offset to start drawing the image at. Note that the
                    // "translate" value is used both for x and y
                    topLeft = Offset(translate, translate)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AnimationStateOrAnimateSheetPreview() {
    AnimationStateOrAnimateSheet()
}
