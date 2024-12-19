package com.droidcon.voices.ui.settings

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.droidcon.voices.R
import com.droidcon.voices.data.preference.FilenameExtension
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(modifier : Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState(SettingsUiState())

    /**
     * Snackbar host for displaying snackbar messages
     */
    val snackbarHostState = remember { SnackbarHostState() }

    /**
     * Tracks the expanded/collapsed state of the drop-down
     */
    val (expanded, onExpandedChange) = remember { mutableStateOf(false) }
    uiState.let{
        Log.d("SettingsScreen", "uiState: ${uiState.filenamePrefix}, ${uiState.filenameExtension}")
    }

    //Handle snackbar messages display
    uiState.userMessage?.let{message->
        val messageText = stringResource(message)
        LaunchedEffect(snackbarHostState, viewModel, message, messageText) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.snackbarMessageShown()
        }

    }

    //Make a scaffold for structure and snackbar message support
    Scaffold(modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {paddingValues->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            //Title
            item {
                Text(
                    text = stringResource(R.string.settings), modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                    ,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            //Divide title from other sections
            item {
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp))
            }

            //Filename prefix field
            item {
                TextField(value = uiState.filenamePrefix,
                    onValueChange = { /*inputFilePrefix = it*/ viewModel.updateFilenamePrefix(it)},
                    placeholder = { Text(stringResource(R.string.filename_prefix)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                    ,
                    label = {
                        Text(stringResource(R.string.filename_prefix_for_recorded_voices))
                    }
                )
            }

            //Filename extension dropdown
            item {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { onExpandedChange(it) },
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    TextField(value = uiState.filenameExtension.name, onValueChange = {}, readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                        ,
                        label = { Text(stringResource(R.string.filename_extension) )},
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }){
                    for (extension in FilenameExtension.values()) {
                        DropdownMenuItem(text = {
                                Text(extension.name)
                        }, onClick = {
                            viewModel.updateFilenameExtension(extension.name)
                            onExpandedChange(false)
                        })
                    }}

                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}