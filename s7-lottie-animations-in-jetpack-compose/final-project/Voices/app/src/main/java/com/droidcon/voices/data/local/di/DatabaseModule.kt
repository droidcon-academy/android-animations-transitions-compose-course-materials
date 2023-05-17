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

package com.droidcon.voices.data.local.di

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Room
import androidx.room.RoomDatabase.MigrationContainer
import androidx.room.migration.Migration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.droidcon.voices.data.local.database.AppDatabase
import com.droidcon.voices.data.local.database.VoiceDao
import javax.inject.Singleton


/**
 * Hilt module that provides a [Room] database for the app
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideVoiceDao(appDatabase: AppDatabase): VoiceDao {
        return appDatabase.voiceDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Voices"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
