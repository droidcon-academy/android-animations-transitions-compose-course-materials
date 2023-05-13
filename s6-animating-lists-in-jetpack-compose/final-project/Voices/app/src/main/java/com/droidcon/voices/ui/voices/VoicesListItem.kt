package com.droidcon.voices.ui.voices

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.droidcon.voices.R
import com.droidcon.voices.data.local.database.Voice
import com.droidcon.voices.ui.util.Util
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * Different transition states for bookmarking
 */
enum class BookmarkStates {Initial, Bookmarked, Disappeared}

@Composable
fun VoicesListItem(
    modifier: Modifier = Modifier,
    activeVoiceId : String?,
    activeVoiceState: ActiveVoiceState,
    voice: Voice,
    onUpdateActiveVoice : (voiceId : String) -> Unit,
    onUpdateActiveVoiceState : (nextState : ActiveVoiceState) -> Unit,
    onShareFile : (path : String) -> Unit,
    onPlayVoice : (path : String) -> Unit,
    onToggleBookmark : (Voice) -> Unit,
    onItemDelete: (voice: Voice) -> Unit
) {
    /**
     * Swipe message to show before and after swipe
     */
    var swipeMessage by remember { mutableStateOf(R.string.swipe_right_to_delete) }

    //TODO: Declare a MutableTransitionState variable initialized to BookmarkStates.Disappeared and remember it
    /**
     * Transition state variable used to handle bookmark transition
     */
    val transitionState = 0


    /**
     * State that signals the caller of [VoicesListItem] to toggle bookmark for [voice]
     */
    var bookmarkToggle by remember { mutableStateOf(false) }


    Surface(modifier = modifier) {
        //Make two nested boxes; the first for holding the guide text that will be visible once swipe is performed
        //The second is for positioning labels
        Box(
            Modifier
                .padding(4.dp)
                .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        //TODO: Set target state to BookmarkStates.Initial
                        bookmarkToggle = true
                    })
                }
        ) {
            //Calling onToggleBookmark directly from inside the double-tap handler didn't work for some reason
            //So I'm using the state signal I set there to call onToggleBookmark here
            if (bookmarkToggle) {
                onToggleBookmark(voice)
                bookmarkToggle = false
            }
            //TODO: Ensure state transitions are sequential


            //TODO: Create the bookmark transition

            //TODO: Define scale using transition
            /**
             * Scale transition to animate the scale of the bookmark icon
             */
            val scale = remember{ 0f }

            //TODO: Define alpha using transition
            /**
             * Alpha transition for the alpha of the bookmark icon
             */
            val alpha = remember{ 0f }

            //TODO: Define bookmark icon rotation using transition
            /**
             * Rotation transition that is used to rotate the bookmark icon
             * Like alpha transition, this is only interested in two states: bookmarked and otherwise
             */
            val bookmarkRotation = remember { 0f }

            Text(stringResource(swipeMessage), Modifier
                .padding(8.dp)
                .align(Alignment.CenterStart),
                style = MaterialTheme.typography.labelLarge
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .swipeRightToDelete(voice) {
                    onItemDelete(it); swipeMessage = R.string.item_deleted_successfully
                }
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )

            ) {
                //The animating heart icon
                //Note that here I am using two different icons to distinguish between adding favorite flag and removing it
                Icon(imageVector = if (voice.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = null, modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .align(Alignment.Center)
                    .fillMaxSize()
                    ,
                    tint = Color.Red
                )

                //The filename and actions
                Row(
                    Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
//                        .background(
//                            color = if (voice.id == activeVoiceId) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
//                            shape = MaterialTheme.shapes.medium
//                        )
                ) {
                    //file name
                    Text(
                        text = voice.fileName,
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp),
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium
                    )

                    //Play button
                    IconButton(
                        onClick = {
                            onUpdateActiveVoice(voice.id)
                            val nextVoiceState =
                                Util.selectNextStateOnTap(activeVoiceState)

                            onUpdateActiveVoiceState(nextVoiceState)
                            onPlayVoice(voice.path)

                        },
                        modifier = Modifier.weight(0.1f)
                    ) {
                        //If the voice is not the current active one, then select the play icon, otherwise select icon based on state
                        Icon(
                            imageVector = if (activeVoiceId != voice.id) Icons.Filled.PlayArrow else Util.selectIconForState(
                                activeVoiceState
                            ),
                            contentDescription = if (activeVoiceId != voice.id) stringResource(
                                R.string.play_voice
                            ) else stringResource(Util.selectTextForState(activeVoiceState)),
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize()
                        )
                    }

                    //Bookmark Button
                    IconButton(
                        onClick = { onToggleBookmark(voice) },
                        modifier = Modifier
                            .weight(0.1f)
                    ) {
                        Icon(
                            imageVector = if (voice.isFavorite) Icons.Filled.BookmarkAdded else Icons.Outlined.BookmarkAdd,
                            contentDescription = if (voice.isFavorite) stringResource(R.string.remove_bookmark) else stringResource(
                                R.string.add_bookmark
                            ),
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationZ = bookmarkRotation
                                }
                        )
                    }

                    //Share Button
                    IconButton(
                        onClick = {
                            onShareFile(voice.path)
                            //                Util.shareFile(context, voice.path)
                        },
                        modifier = Modifier
                            .weight(0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.share),
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxSize()
                        )
                    }

                }
                //The size label
                Text(
                    text = "${voice.fileSize / 1_000} KB",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 4.dp)
                        .align(Alignment.BottomStart)
                )

                //The duration label
                Text(
                    text = Util.msToTimeStr(voice.duration),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .padding(start = 16.dp, bottom = 4.dp)
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

private fun Modifier.swipeRightToDelete(voice: Voice, onItemDelete: (voice : Voice) -> Unit) = composed {
    val offsetX = remember { Animatable(0f) }
    //TODO: Detect drag gesture and animate offset accordingly
    pointerInput(Unit){
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while(true){
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                val velocityTracker = VelocityTracker()

                offsetX.stop()
                awaitPointerEventScope {
                    horizontalDrag(pointerId){inputChange->
                        launch{
                            if (inputChange.positionChange().x >= 0){
                                offsetX.snapTo(offsetX.value + inputChange.positionChange().x)
                            }
                        }
                        velocityTracker.addPosition(timeMillis = inputChange.uptimeMillis, position = inputChange.position)


                    }
                }
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(initialValue = offsetX.value, initialVelocity = velocity)
                offsetX.updateBounds(lowerBound = - size.width.toFloat() * 0.1f, upperBound = size.width.toFloat())

                if (targetOffsetX <= size.width){
                    offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                } else {
                    offsetX.animateDecay(initialVelocity = velocity, animationSpec = decay)
                    onItemDelete(voice)
                }
            }
        }
    }
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
}

@Preview
@Composable
fun VoicesListItemPreview() {
    VoicesListItem(
        activeVoiceId = null,
        activeVoiceState = ActiveVoiceState.IDLE,
        voice = Voice("My Voice.mp3", "/My Voice.mp3"),
        onUpdateActiveVoice = {},
        onUpdateActiveVoiceState = {},
        onShareFile = {},
        onPlayVoice = {} ,
        onToggleBookmark = {},
        onItemDelete = {}
    )
}