package com.prachaarbot.di

import android.content.Context
import androidx.room.Room
import com.prachaarbot.data.local.room.dao.CallDao
import com.prachaarbot.data.local.room.database.AppDatabase
import com.prachaarbot.data.local.sharedPreference.AppSharedPreferenceProvider
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {
    @Provides
    @Singleton
    internal fun provideLocalSharedPref(appSharedPreferenceProvider: AppSharedPreferenceProvider) =
        LocalPrefData(appSharedPreferenceProvider)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "CALL_LOGS"
        ).build()
    }

    @Provides
    fun provideYourDao(appDatabase: AppDatabase): CallDao {
        return appDatabase.callDao()
    }
}