package com.korett.zulip_client.core.ui.mapper

import com.korett.zulip_client.core.domain.use_cases.chat_messages.ChatMessages
import com.korett.zulip_client.core.ui.model.ChatMessagesUi


fun ChatMessages.toChatMessagesUi(): ChatMessagesUi =
    ChatMessagesUi(
        ownMessages = ownMessages.map { it.toOwnMessageUi() },
        anotherMessages = anotherMessages.map { it.toAnotherMessageUi() }
    )