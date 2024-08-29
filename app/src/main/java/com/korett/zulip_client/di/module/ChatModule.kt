package com.korett.zulip_client.di.module

import com.korett.zulip_client.core.domain.repository.ChatRepository
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedChangedMessages
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedChatMessagesUseCase
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedPreviousChatMessagesUseCase
import com.korett.zulip_client.presentation.chat.ChatActor
import com.korett.zulip_client.presentation.chat.ChatStoreFactory
import com.korett.zulip_client.presentation.chat.mapper.ChatStateMapper
import com.korett.zulip_client.presentation.chat.mapper.ChatStateMapperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ChatModule {

    @Binds
    fun bindChatStateMapper(chatStateMapperImpl: ChatStateMapperImpl): ChatStateMapper

    companion object {
        @Provides
        fun provideChatStoreFactory(chatActor: ChatActor): ChatStoreFactory {
            return ChatStoreFactory(chatActor)
        }

        @Provides
        fun provideChatActor(
            chatRepository: ChatRepository,
            getSortedChatMessagesUseCase: GetSortedChatMessagesUseCase,
            getSortedPreviousChatMessagesUseCase: GetSortedPreviousChatMessagesUseCase,
            getSortedChangedMessages: GetSortedChangedMessages
        ): ChatActor {
            return ChatActor(
                chatRepository,
                getSortedChatMessagesUseCase,
                getSortedPreviousChatMessagesUseCase,
                getSortedChangedMessages
            )
        }
    }

}