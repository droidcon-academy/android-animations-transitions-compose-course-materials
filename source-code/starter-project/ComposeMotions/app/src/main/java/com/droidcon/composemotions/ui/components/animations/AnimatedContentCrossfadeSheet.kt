package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R
import com.droidcon.composemotions.ui.theme.ComposeMotionsTheme

/**
 * Duration of the slide animation used in *AnimatedContent*
 */
internal const val DURATION = 800

/**
 * Demo animation for Jetpack Compose's AnimatedContent() and Crossfade APIs
 *
 * This composable demonstrates the use of [AnimatedContent] and [Crossfade]
 * animation APIs in Jetpack Compose.
 * A group of thumbnails is displayed to the user to select from. When the user selects a thumbnail,
 * the corresponding image is displayed in the two large Image composables.
 * The first [Image] animates between the images using [AnimatedContent] and
 * the second [Image] animates between the old image and the newly selected image
 * using the [Crossfade] composable.
 * The current selected image index is tracked using a mutable state variable.
 * Note the custom [androidx.compose.animation.ContentTransform] lambda used to create the
 * slide effect in the first composable and the custom [tween] animation used for the cross-fading images.
 */
@Composable
fun AnimatedContentCrossfadeSheet() {
    val images = listOf(R.drawable.dog1, R.drawable.dog2, R.drawable.cat1, R.drawable.cat2)
    var currentIndex by remember{ mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        //TODO: Wrap with AnimatedContent
        Image(
            painter = painterResource(images[currentIndex]), stringResource(R.string.image),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        LazyRow(
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth()
        ) {
            itemsIndexed(items = images) { i, item ->
                Image(
                    painter = painterResource(id = item), stringResource(R.string.image),
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            color = MaterialTheme.colorScheme.tertiary,
                            width = if (i == currentIndex) 4.dp else 0.dp
                        )
                        .clickable {
                            currentIndex = i
                        },
                    contentScale = ContentScale.Crop
                )

            }
        }

        //TODO: Wrap with Crossfade
        Image(
            painter = painterResource(images[currentIndex]), stringResource(R.string.image),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

    }
}

@Preview
@Composable
fun AnimatedContentSheetPreview() {
    ComposeMotionsTheme {
        AnimatedContentCrossfadeSheet()
    }
}