package com.droidcon.voices.ui.voices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.droidcon.voices.R
import com.droidcon.voices.data.local.database.Voice
import com.droidcon.voices.ui.theme.labelTiny
import com.droidcon.voices.ui.util.Util

@Composable
fun VoiceListItem(
    modifier: Modifier = Modifier,
    activeVoiceId : String?,
    activeVoiceState: ActiveVoiceState,
    voice: Voice,
    onUpdateActiveVoice : (voiceId : String) -> Unit,
    onUpdateActiveVoiceState : (nextState : ActiveVoiceState) -> Unit,
    onShareFile : (path : String) -> Unit,
    onPlayVoice : (path : String) -> Unit,
    onToggleBookmark : (voice : Voice) -> Unit
    ) {
    Box(modifier = modifier) {
        //The filename and actions
        Row(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = if (voice.id == activeVoiceId) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
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
                modifier = Modifier.weight(0.15f)
            ) {
                //If the voice is not the current active one, then select the play icon, otherwise select icon based on state
                Icon(
                    imageVector = if (activeVoiceId != voice.id) Icons.Filled.PlayArrow else Util.selectIconForState(
                        activeVoiceState
                    ),
                    contentDescription = if (activeVoiceId != voice.id) stringResource(
                        R.string.play_voice
                    ) else stringResource(Util.selectTextForState(activeVoiceState)),
                    modifier = Modifier.padding(8.dp)
                )
            }

            //Bookmark Button
            IconButton(onClick = { onToggleBookmark(voice) }) {
                Icon(
                    imageVector = if (voice.isFavorite) Icons.Filled.BookmarkAdded else Icons.Outlined.BookmarkAdd,
                    contentDescription = if (voice.isFavorite) stringResource(R.string.remove_bookmark) else stringResource(
                        R.string.add_bookmark
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(0.15f)
                )
            }

            //Share Button
            IconButton(
                onClick = {
                    onShareFile(voice.path)
                    //                Util.shareFile(context, voice.path)
                },
                modifier = Modifier
                    .weight(0.15f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(R.string.share)
                )
            }

        }
        //The size label
        Text(text = "${voice.fileSize/1_000} KB",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 4.dp)
                .align(Alignment.BottomStart)
        )

        Text(text = Util.msToTimeStr(voice.duration),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 4.dp)
                .align(Alignment.BottomCenter)
        )
    }
}