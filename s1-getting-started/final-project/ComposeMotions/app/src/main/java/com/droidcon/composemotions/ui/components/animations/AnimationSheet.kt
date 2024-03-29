package com.droidcon.composemotions.ui.components.animations


import android.util.Log
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R
import com.droidcon.composemotions.ui.theme.ComposeMotionsTheme
import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Switch
import kotlin.math.roundToInt


/**
 * Duration of the animation created with [TargetBasedAnimation]
 */
const val ANIM_DURATION = 5000

/**
 * Demo composable showing the usage of [Animation] API,
 * which is part of Jetpack Compose core animation APIs.
 */
@Composable
fun AnimationSheet() {
    /**
     * Animating value that will be mutated by [TargetBasedAnimation]
     */
    var animValue by remember{mutableStateOf(0)}

    /**
     * Variable used to keep track of animation time
     */
    var time by remember{mutableStateOf(0L)}


    /**
     * Handles switch state change
     */
    var activated by remember { mutableStateOf(false) }

    /**
     * Value of DecayAnimation
     */
    var decayValue by remember{ mutableStateOf(0f) }

    /**
     * Tracks the time of the decay animation
     */
    var decayTime by remember { mutableStateOf(0L) }

    /**
     * Animation object created using [TargetBasedAnimation] animation wrapper class
     */
    val anim = remember{
        TargetBasedAnimation(
            animationSpec = tween(ANIM_DURATION),
            typeConverter = Int.VectorConverter,
            initialValue = 500,
            initialVelocity = 500,
            targetValue = -500
        )
    }


    //Decay for decaying the animation
    val decay = remember {
        DecayAnimation(
            animationSpec = exponentialDecay(),
            typeConverter = Float.VectorConverter,
            initialValue = 100f,
            initialVelocity = 4000f
        )
    }

    //Launch decay animation
    LaunchedEffect(activated){
        val start = withFrameNanos { it }
        do {
            decayTime = withFrameNanos { it } - start
            decayValue = decay.getValueFromNanos(decayTime)
            //Run as long as animation is below 8 seconds
        } while(decayTime < 8000 * 1_000_000L) //Convert to nanoseconds
    }

    //Launch the value animation into the composition's coroutine context
    LaunchedEffect(activated){
        val start = withFrameNanos { it }
        do {
            time = withFrameNanos { it } - start
            animValue = anim.getValueFromNanos(time)
            Log.d("AnimationSheet", "time: $time, value: $animValue")
            //Convert milliseconds to nano-seconds and use it as condition to stop animation
        } while ( animValue > -500)
    }


    Column(Modifier.fillMaxSize()){
        Switch(modifier = Modifier
            .padding(16.dp)
            .size(50.dp)
            .align(Alignment.CenterHorizontally)
            ,
            checked = activated,
            onCheckedChange = { activated = it }
        )
        //The offset of the following box will be modified using the animated value
        Box(Modifier
            .offset { IntOffset(0, decayValue.roundToInt()) }
            .padding(top = 16.dp)
            .width(200.dp)
            .height(100.dp)
            .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer)
            .align(Alignment.CenterHorizontally)
            .background(MaterialTheme.colorScheme.tertiary)
        ){
            Text(stringResource(R.string.decayanimation), Modifier.padding(8.dp).align(Alignment.TopCenter), style = MaterialTheme.typography.labelMedium)
            Text(text = stringResource(R.string.animation_info, decayTime, decayValue.toInt()), modifier = Modifier
                .align(Alignment.Center),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
        }

        //2nd Box - The offset of the following box will be modified using the animated value
        Box(Modifier
            .offset { IntOffset(0, animValue) }
            .padding(top = 300.dp)
            .width(200.dp)
            .height(100.dp)
            .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer)
            .align(Alignment.CenterHorizontally)
            .background(MaterialTheme.colorScheme.primary)
        ){
            Text(stringResource(R.string.targetbasedanimation), Modifier.padding(8.dp).align(Alignment.TopCenter), style = MaterialTheme.typography.labelMedium)
            Text(text = stringResource(R.string.animation_info, animValue, animValue), modifier = Modifier
                .align(Alignment.Center),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
        }


        
    }


}

@Preview(showBackground = true)
@Composable
fun AnimationSheetPreview() {
    ComposeMotionsTheme {
        AnimationSheet()
    }
}
