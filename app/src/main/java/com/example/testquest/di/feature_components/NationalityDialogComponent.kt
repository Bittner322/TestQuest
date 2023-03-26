package com.example.testquest.di.feature_components

import com.example.testquest.di.AppComponent
import com.example.testquest.di.scopes.ScreenScope
import com.example.testquest.presentation.dialogs.NationalityDialog
import dagger.Component

@Component(dependencies = [AppComponent::class])
@ScreenScope
interface NationalityDialogComponent {

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent) : NationalityDialogComponent
    }

    fun inject(dialog: NationalityDialog)
}