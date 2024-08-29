package com.korett.zulip_client.core.domain.use_cases.chat_messages

import com.korett.zulip_client.core.domain.model.MessageModel

data class ChatMessages (
    val ownMessages: List<MessageModel>,
    val anotherMessages: List<MessageModel>
)