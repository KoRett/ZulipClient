package com.korett.zulip_client.di.component

import com.korett.zulip_client.di.module.StreamTabModule
import com.korett.zulip_client.di.scope.FragmentScope
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.StreamTabFragment
import dagger.Component

@FragmentScope
@Component(
    modules = [StreamTabModule::class],
    dependencies = [AppComponent::class]
)
interface StreamTabComponent {
    fun inject(streamTabFragment: StreamTabFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): StreamTabComponent
    }
}