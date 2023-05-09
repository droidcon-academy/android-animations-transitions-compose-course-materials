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

package com.droidcon.voices.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.droidcon.voices.data.local.database.Voice
import com.droidcon.voices.data.local.database.VoiceDao

/**
 * Unit tests for [DefaultVoicesRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultVoicesRepositoryTest {

    @Test
    fun voices_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultVoicesRepository(FakeVoiceDao())

        repository.addVoice(Voice("Untitled", ""))

        assertEquals(repository.voices.first().size, 1)
    }

}

private class FakeVoiceDao : VoiceDao {

    private val data = mutableListOf<Voice>()

    override fun getVoices(): Flow<List<Voice>> = flow {
        emit(data)
    }

    override suspend fun insertVoice(item: Voice) {
        data.add(0, item)
    }

    override fun observeVoices(): Flow<List<Voice>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateVoice(vararg voice: Voice) {
        TODO("Not yet implemented")
    }
}
