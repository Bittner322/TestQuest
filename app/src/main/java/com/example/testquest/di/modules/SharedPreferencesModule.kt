package com.example.testquest.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.testquest.QuestApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val SHARED_PREFS_NAME = "sharedPrefs"

@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(application: QuestApplication): SharedPreferences {
        return application.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
}