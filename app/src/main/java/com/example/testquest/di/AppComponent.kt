package com.example.testquest.di

import android.content.Context
import android.content.SharedPreferences
import com.example.testquest.QuestApplication
import com.example.testquest.data.network.RetrofitProvider
import com.example.testquest.di.modules.AppModule
import com.example.testquest.di.modules.SharedPreferencesModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [SharedPreferencesModule::class, AppModule::class])
@Singleton
interface AppComponent {
    val retrofitProvider: RetrofitProvider
    val sharedPreferences: SharedPreferences
    fun application(): QuestApplication
    fun context(): Context

    fun inject(application: QuestApplication)
}