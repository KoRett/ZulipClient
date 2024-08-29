package com.korett.zulip_client.di.component

import com.korett.zulip_client.di.module.ChatModule
import com.korett.zulip_client.di.scope.FragmentScope
import com.korett.zulip_client.presentation.chat.ChatFragment
import dagger.BindsInstance
import dagger.Component
import java.util.Locale

@FragmentScope
@Component(
    modules = [ChatModule::class],
    dependencies = [AppComponent::class]
)
interface ChatComponent {

    fun inject(chatFragment: ChatFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent, @BindsInstance locale: Locale): ChatComponent
    }
}