package com.example.testquest.di.feature_components

import com.example.testquest.di.AppComponent
import com.example.testquest.di.scopes.ScreenScope
import com.example.testquest.presentation.settings_activity.SettingsActivity
import dagger.Component

@Component(dependencies = [AppComponent::class])
@ScreenScope
interface SettingsActivityComponent {

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent) : SettingsActivityComponent
    }

    fun inject(activity: SettingsActivity)
}