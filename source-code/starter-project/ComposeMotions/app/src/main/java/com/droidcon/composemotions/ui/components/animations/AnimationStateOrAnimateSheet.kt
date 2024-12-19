package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animate
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
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
            0
            //TODO: Make an AnimationState for Color with initial value of Cyan
        }

        //Animate background color using "AnimationState"
        LaunchedEffect(Unit) {
            //Takes initial color from AnimationState and animates to the specified color
            //TODO: Animate to Green repeatedly
        }

        //Animate translate using "animate"
        //Unlike "AnimationState"", "animate" specifies both initial and target value for the animation
        LaunchedEffect(Unit) {
            //TODO: Animate "translate" to 200f using "animate()"
        }

        //Container for "AnimationState"
        //Here, the value of AnimationState will be used to set the background color
        Box(
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .weight(1f)
                .drawBehind{
                    //TODO: Update Color to the value obtained from animationState
                    drawRect(Color.Red)
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

@Preview
@Composable
fun AnimationStateOrAnimateSheetPreview() {
    AnimationStateOrAnimateSheet()
}
