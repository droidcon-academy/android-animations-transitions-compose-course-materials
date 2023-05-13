/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.droidcon.voices.ui.voices

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.droidcon.voices.R
import com.droidcon.voices.data.preference.SortBy
import com.droidcon.voices.data.preference.SortOrder
import com.droidcon.voices.ui.theme.VoicesAppTheme
import com.droidcon.voices.ui.util.Util
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.google.android.exoplayer2.ui.StyledPlayerView
import java.io.File

internal const val TAG = "VoiceScreen"

/**
 * Screen for displaying the list of voices
 */
@Composable
fun VoicesScreen(modifier: Modifier = Modifier, viewModel: VoicesViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState(
        initialValue = VoicesUiState(),
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    /**
     * Context for context-related operations
     */
    val context = LocalContext.current

    /**
     * Snackbar host state for displaying snackbar messages
     */
    val snackbarHostState = remember { SnackbarHostState() }

    /**
     * [ExoPlayer] instance used for playing the the voices
     */
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        STATE_ENDED -> viewModel.updateActiveVoiceState(ActiveVoiceState.IDLE)
                        else -> {}
                    }
                }
            })
        }
    }
    // Track expanded / collapsed state of the sort-by drop-down
    val (sortByExpanded, onSortByExpandedChange) = remember { mutableStateOf(false) }

    // Keep track of sort-order dropdown's expansion / collapse
    val (sortOrderExpanded, onSortOrderExpandedChange) = remember { mutableStateOf(false) }


    uiState.userMessage?.let{message->
        val messageText = stringResource(message)
        LaunchedEffect(message, messageText, viewModel, snackbarHostState){
            snackbarHostState.showSnackbar(messageText)
            viewModel.snackbarMessageShown()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.voices)) }, actions = {
                //Sort-by dropdown
                ExposedDropdownMenuBox(
                    expanded = sortByExpanded,
                    onExpandedChange = { onSortByExpandedChange(it) },
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .wrapContentHeight()
                        .weight(0.2f)
                ) {
                    TextField(
                        value = uiState.sortBy.name,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor(),
                        label = { Text(stringResource(R.string.sort_by)) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        textStyle = MaterialTheme.typography.labelSmall
                    )
                    DropdownMenu(expanded = sortByExpanded, onDismissRequest = {
                        onSortByExpandedChange(false)
                    }) {
                        for (sortBy in SortBy.values()) {
                            DropdownMenuItem(text = { Text(sortBy.name) }, onClick = {
                                viewModel.setSortBy(sortBy)
                                onSortByExpandedChange(false)
                            },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
                //Sort-by drop down
                ExposedDropdownMenuBox(
                    expanded = sortOrderExpanded,
                    onExpandedChange = { onSortOrderExpandedChange(it) },
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .wrapContentHeight()
                        .weight(0.2f)
                ) {
                    TextField(
                        value = uiState.sortOrder.name,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor(),
                        label = { Text(stringResource(R.string.sort)) },
                        trailingIcon = { Icon(Icons.Filled.Sort, Icons.Filled.Sort.name) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        textStyle = MaterialTheme.typography.labelSmall
                    )
                    DropdownMenu(expanded = sortOrderExpanded, onDismissRequest = {
                        onSortOrderExpandedChange(false)
                    }) {
                        for (sortOrder in SortOrder.values()) {
                            DropdownMenuItem(text = { Text(sortOrder.name) }, onClick = {
                                viewModel.setSortOrder(sortOrder)
                                onSortOrderExpandedChange(false)
                            },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

            },
                colors = TopAppBarDefaults.smallTopAppBarColors()
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val (lazyColumn, playerView) = createRefs()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(lazyColumn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                verticalArrangement = Arrangement.spacedBy(space = 4.dp)
            ) {
                item {
                    if (uiState.isLoading) {
                        Text(
                            stringResource(R.string.loading_voices_please_wait),
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                item {
                    if (uiState.items.isEmpty()) {
                        Text(text = stringResource(R.string.recorded_voices_will_be_displayed_here),
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                //TODO: Pass unique key
                itemsIndexed(items = uiState.items) { _, voice ->
                    VoicesListItem(
                        modifier = Modifier
                        //TODO: Add animateItemPlacement
                        ,
                        activeVoiceId = uiState.activeVoiceId,
                        activeVoiceState = uiState.activeVoiceState,
                        voice = voice,
                        onUpdateActiveVoice = { viewModel.updateActiveVoice(it) },
                        onUpdateActiveVoiceState = { viewModel.updateActiveVoiceState(it) },
                        onShareFile = { path-> Util.shareFile(context, path) },
                        onPlayVoice = { path ->
                            try {
                                val mediaItem = MediaItem.fromUri(path)
                                player.clearMediaItems()
                                player.addMediaItem(mediaItem)
                                player.prepare()
                                if (player.isPlaying) {
                                    player.pause()
                                } else {
                                    player.play()
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Play button: ${e.message}")
                            }
                        },
                        onToggleBookmark = { _voice -> viewModel.toggleBookmark(_voice)},
                        onItemDelete = {_voice->
                            try {
                                if (File(_voice.path).delete()) {
                                    viewModel.deleteVoice(_voice)
                                    viewModel.showSnackbarMessage(R.string.item_deleted_successfully)
                                }
                                else {
                                    viewModel.showSnackbarMessage(R.string.failed_to_delete_item)
                                }
                            } catch (e: SecurityException) {
                                Log.e(TAG, "VoicesScreen: Failed to delete voice file : ${e.message}", )
                            }
                        }
                    )
                }
            }
            //Show player only when player is in playing or paused state
            if (uiState.activeVoiceState == ActiveVoiceState.Playing ||
                uiState.activeVoiceState == ActiveVoiceState.Paused
            ) {
                AndroidView(modifier = Modifier
                    .height(Util.PlayerViewHeight)
                    .constrainAs(playerView) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    factory = {
                        StyledPlayerView(it).apply {
                            setPlayer(player)
                        }
                    }, update = {

                    })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    VoicesAppTheme {
        VoicesScreen()
    }
}
