package com.korett.zulip_client.di.component

import android.content.Context
import com.korett.zulip_client.core.domain.repository.ChatRepository
import com.korett.zulip_client.core.domain.repository.StreamRepository
import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.di.module.DataModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataModule::class]
)
interface AppComponent {
    val userRepository: UserRepository
    val chatRepository: ChatRepository
    val streamRepository: StreamRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}