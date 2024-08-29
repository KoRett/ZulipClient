package com.korett.zulip_client.di.component

import com.korett.zulip_client.di.module.AnotherProfileModule
import com.korett.zulip_client.di.scope.FragmentScope
import com.korett.zulip_client.presentation.another_profile.AnotherProfileFragment
import dagger.Component

@FragmentScope
@Component(
    modules = [AnotherProfileModule::class],
    dependencies = [AppComponent::class]
)
interface AnotherProfileComponent {

    fun inject(anotherProfileFragment: AnotherProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): AnotherProfileComponent
    }

}