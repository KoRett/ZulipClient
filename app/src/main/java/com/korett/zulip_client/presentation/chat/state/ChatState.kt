package com.korett.zulip_client.presentation.chat.state

import com.korett.zulip_client.core.ui.model.ChatMessagesUi
import com.korett.zulip_client.core.ui.utils.LceState

data class ChatState(
    val chatMessagesState: LceState<ChatMessagesUi>,
    val streamName: String,
    val topicName: String,
    val selectedMessageId: Int?,
    val previousMessagesState: PreviousMessagesState,
    val previousMessageCount: Int,
    val initialMessageCount: Int
)

sealed class PreviousMessagesState {
    data object Error : PreviousMessagesState()
    data object Loading : PreviousMessagesState()
    data object Waiting : PreviousMessagesState()
    data object End : PreviousMessagesState()
}