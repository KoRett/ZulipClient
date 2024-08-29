package com.korett.zulip_client.presentation.chat

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.chat.state.ChatState
import com.korett.zulip_client.presentation.chat.state.PreviousMessagesState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class ChatStoreFactory(private val actor: ChatActor) {
    fun create(streamName: String, topicName: String): Store<ChatEvent, ChatEffect, ChatState> {
        return ElmStore(
            initialState = ChatState(
                chatMessagesState = LceState.Loading,
                streamName,
                topicName,
                previousMessagesState = PreviousMessagesState.Waiting,
                selectedMessageId = null,
                previousMessageCount = 20,
                initialMessageCount = 50
            ),
            reducer = ChatReducer(),
            actor = actor
        )
    }
}