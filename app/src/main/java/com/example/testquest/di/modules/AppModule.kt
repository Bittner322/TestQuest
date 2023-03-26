package com.example.testquest.di.modules

import android.content.Context
import com.example.testquest.QuestApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: QuestApplication) {
    @Provides
    @Singleton
    fun providesApplication(): QuestApplication = application

    @Provides
    @Singleton
    fun providesApplicationContext(): Context = application
}