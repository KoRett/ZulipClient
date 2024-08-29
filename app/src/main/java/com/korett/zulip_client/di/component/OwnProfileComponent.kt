package com.korett.zulip_client.di.component

import com.korett.zulip_client.di.module.OwnProfileModule
import com.korett.zulip_client.di.scope.FragmentScope
import com.korett.zulip_client.presentation.tabs.own_profile.OwnProfileFragment
import dagger.Component

@FragmentScope
@Component(
    modules = [OwnProfileModule::class],
    dependencies = [AppComponent::class]
)
interface OwnProfileComponent {
    fun inject(ownProfileFragment: OwnProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): OwnProfileComponent
    }

}