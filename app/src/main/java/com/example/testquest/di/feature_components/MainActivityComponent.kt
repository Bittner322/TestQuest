package com.example.testquest.di.feature_components

import com.example.testquest.di.AppComponent
import com.example.testquest.di.scopes.ScreenScope
import com.example.testquest.presentation.main_activity.MainActivity
import dagger.Component

@Component(dependencies = [AppComponent::class])
@ScreenScope
interface MainActivityComponent {

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent) : MainActivityComponent
    }

    fun inject(activity: MainActivity)

}