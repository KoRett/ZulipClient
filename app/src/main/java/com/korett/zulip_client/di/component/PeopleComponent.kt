package com.korett.zulip_client.di.component

import com.korett.zulip_client.di.module.PeopleModule
import com.korett.zulip_client.di.scope.FragmentScope
import com.korett.zulip_client.presentation.tabs.people.PeopleFragment
import dagger.Component

@FragmentScope
@Component(
    modules = [PeopleModule::class],
    dependencies = [AppComponent::class]
)
interface PeopleComponent {

    fun inject(peopleFragment: PeopleFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): PeopleComponent
    }

}