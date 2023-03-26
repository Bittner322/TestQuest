package com.example.testquest

import android.app.Application
import com.example.testquest.data.repositories.BaseApiRepository
import com.example.testquest.di.ComponentStorage
import com.example.testquest.di.DaggerAppComponent
import com.example.testquest.di.initRootComponent
import com.example.testquest.di.modules.AppModule
import javax.inject.Inject

class QuestApplication: Application() {

    @Inject
    lateinit var baseApiRepository: BaseApiRepository

    companion object {

        private lateinit var instance: QuestApplication

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ComponentStorage.initRootComponent {
            DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        }.inject(this)

        baseApiRepository.setCurrentBaseApi()
    }
}