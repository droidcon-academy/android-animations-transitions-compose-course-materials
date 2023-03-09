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

package com.droidcon.voices.ui.voices


import androidx.lifecycle.SavedStateHandle
import com.droidcon.voices.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.droidcon.voices.data.VoicesRepository
import com.droidcon.voices.data.di.fakeVoices
import com.droidcon.voices.data.local.database.Voice

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class VoicesViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = VoicesViewModel(FakeVoicesRepository(flow{emit(fakeVoices)}), SavedStateHandle())
        val voice = fakeVoices[0]
        assertEquals(viewModel.uiState.value.items.first(), voice)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = VoicesViewModel(FakeVoicesRepository(flow{emit(fakeVoices)}), SavedStateHandle())
        assertEquals(viewModel.uiState.value.isLoading, true)
    }
}

private class FakeVoicesRepository(override val voices: Flow<List<Voice>>) : VoicesRepository {

    private val data = mutableListOf<String>()

    override fun getVoicesStream(): Flow<Result<List<Voice>>> {
        TODO("Not yet implemented")
    }

    override suspend fun addVoice(voice: Voice) {
        TODO("Not yet implemented")
    }

    override suspend fun updateVoice(voice: Voice) {
        TODO("Not yet implemented")
    }

}
