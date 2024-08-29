package com.korett.zulip_client.core.domain.use_cases.chat_messages.mapper

import com.korett.zulip_client.core.domain.model.MessageModel
import com.korett.zulip_client.core.domain.use_cases.chat_messages.ChatMessages

fun mapMessageModelsToChatMessages(messages: List<MessageModel>, usersOwnId: Int): ChatMessages {
    val ownMessages: MutableList<MessageModel> = mutableListOf()
    val anotherMessages: MutableList<MessageModel> = mutableListOf()

    messages.forEach { messageModel ->
        if (messageModel.senderId == usersOwnId) {
            ownMessages.add(messageModel)
        } else {
            anotherMessages.add(messageModel)
        }
    }

    return ChatMessages(ownMessages, anotherMessages)
}