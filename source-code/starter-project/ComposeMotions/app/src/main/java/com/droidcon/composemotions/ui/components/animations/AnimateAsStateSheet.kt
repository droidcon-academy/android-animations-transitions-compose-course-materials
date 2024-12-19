package com.droidcon.composemotions.ui.components.animations

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ToggleOff
import androidx.compose.material.icons.outlined.ToggleOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.droidcon.composemotions.R

/**
 * Corner size change animation duration
 */
const val CORNER_DURATION = 1000

/**
 * Color animation duration
 */
const val COLOR_DURATION = 1000

/**
 * Demo animation for Jetpack Compose animate*AsState APIs
 * This is a high-level animation and very easy to use. Just specify the desired target
 * values for each state and Compose will animate between them.
 * You can animate many types including [Int], [Size], [Float], [Color], [Offset], [Dp], etc. this way.
 * Here, animation of Float, Color, and Dp is shown.
 */
@Composable
fun AnimateAsStateSheet() {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        //State object to track selection by the user
        val (selected, onSelectedChange) = remember { mutableStateOf(false) }

        /**
         * Image resource to be used with Image
         */
        val imageRes by remember { mutableStateOf(R.drawable.dog2) }

        //TODO: Add dp
        /**
         * Animated corner size for image
         */
        val cornerSize = 0.dp

        //TODO: Animate color
        /**
         * Animated color used for filtering the image
         */
        val filterColor = Color.Transparent

        //TODO: Add animated scale
        /**
         * Animated scale for Image
         */
        val scale = 1f

        Box(
            Modifier
                .weight(1f)
                .padding(8.dp)
                .clip(RoundedCornerShape(topStart = cornerSize)) //Apply animated corner size
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(imageRes),
                contentDescription = stringResource(R.string.cat_image_1),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Text(
                stringResource(R.string.animate_dp_corner_size),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(8.dp)

            )
        }

        //Animate Color: Filter
        Box(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(imageRes),
                contentDescription = stringResource(R.string.cat_image_1),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(),
                colorFilter = ColorFilter.tint(filterColor, blendMode = BlendMode.Difference)
            )
            Text(
                stringResource(R.string.animate_color_filter_color),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(8.dp)

            )
        }

        //Animate Float: Scale
        Box(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(imageRes),
                contentDescription = stringResource(R.string.cat_image_1),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
            )
            Text(
                stringResource(R.string.animate_float_scale),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(8.dp)

            )
        }


        //Show all animated values at once
        Box(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(imageRes),
                contentDescription = stringResource(R.string.cat_image_1),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
                    .clip(RoundedCornerShape(cornerSize)),
                colorFilter = ColorFilter.tint(filterColor, blendMode = BlendMode.Difference)
            )
            Text(
                stringResource(R.string.animate_corner_color_scale),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(8.dp)
            )
        }

        //Toggle button that allows switching states
        IconToggleButton(
            checked = selected, onCheckedChange = { onSelectedChange(it) },
            modifier = Modifier
                .padding(8.dp)
        ) {
            if (selected) {
                Icon(Icons.Outlined.ToggleOn, stringResource(R.string.toggle_on))
            } else {
                Icon(Icons.Outlined.ToggleOff, stringResource(R.string.toggle_off))
            }
        }

    }
}

@Preview
@Composable
fun AnimateAsStateSheetPreview() {
    AnimateAsStateSheet()
}