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

package com.droidcon.voices.data.di

import com.droidcon.voices.data.DefaultVoicesRepository
import com.droidcon.voices.data.Result
import com.droidcon.voices.data.VoicesRepository
import com.droidcon.voices.data.local.database.Voice
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This hilt module binds some hilt components for the app
 */
@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsVoiceRepository(
        voiceRepository: DefaultVoicesRepository
    ): VoicesRepository
}


class FakeVoicesRepository @Inject constructor() : VoicesRepository {
    override val voices: Flow<List<Voice>> = flowOf(fakeVoices)

    override suspend fun addVoice(voice: Voice) {
        throw NotImplementedError()
    }

    override suspend fun updateVoice(voice: Voice) {
        throw NotImplementedError()
    }

    override suspend fun deleteVoice(vararg voice: Voice) {
        throw NotImplementedError()
    }

    override fun getVoicesStream(): Flow<Result<List<Voice>>> {
        return flowOf(
            Result.Success(fakeVoices)
        )
    }
}

val fakeVoices = listOf(
    Voice("Voice 1", ""),
    Voice("Voice 2", ""),
    Voice("Voice 3", "")
)
