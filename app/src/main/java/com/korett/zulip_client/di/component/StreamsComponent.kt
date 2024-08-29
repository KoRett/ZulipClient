package com.korett.zulip_client.di.component

import com.korett.zulip_client.di.module.StreamsModule
import com.korett.zulip_client.di.scope.FragmentScope
import com.korett.zulip_client.presentation.tabs.streams.StreamsFragment
import dagger.Component

@FragmentScope
@Component(
    modules = [StreamsModule::class],
    dependencies = [AppComponent::class]
)
interface StreamsComponent {
    fun inject(streamsFragment: StreamsFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): StreamsComponent
    }
}