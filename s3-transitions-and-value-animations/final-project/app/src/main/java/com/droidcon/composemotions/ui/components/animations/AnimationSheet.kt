package com.droidcon.composemotions.ui.components.animations

import android.util.Log
import androidx.compose.animation.core.Animation
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
    var animValue by remember{ mutableStateOf(0) }

    /**
     * Variable used to keep track of animation time
     */
    var time by remember{ mutableStateOf(0L) }


    /**
     * Animation object created using [TargetBasedAnimation] animation wrapper class
     */
    val anim = remember {0}
    //TODO: Create a TargetBasedAnimation and remember it



    //Launch the value animation into the composition's coroutine context
    LaunchedEffect(Unit){
        //TODO: Launch the animation for ANIM_DURATION and grow animValue
    }

    Column(Modifier.fillMaxSize()){
        //The offset of the following box will be modified using the animated value
        Box(Modifier
            .offset { IntOffset(0, animValue) }
            .padding(top = 16.dp)
            .size(200.dp)
            .border(2.dp, MaterialTheme.colorScheme.onPrimaryContainer)
            .align(Alignment.CenterHorizontally)
        ){
            Text(text = stringResource(R.string.animation_info, time, animValue), modifier = Modifier
                .align(Alignment.Center),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
        }

    }
}

@Preview
@Composable
fun AnimationSheetPreview() {
    Box {
        AnimationSheet()
    }
}