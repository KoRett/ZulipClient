package com.korett.zulip_client.core.data.remote_source.mapper

import com.korett.zulip_client.core.data.remote_source.model.MessageModelNetwork
import com.korett.zulip_client.core.domain.model.MessageModel

fun MessageModelNetwork.toMessageDomain(usersOwnId: Int): MessageModel =
    MessageModel(
        id = id,
        senderId = senderId,
        avatarUrl = avatarUrl,
        senderFullName = senderFullName,
        content = content,
        timestamp = timestamp,
        streamName = displayRecipient,
        topicName = subject,
        reactions = mapReactionsRemoteToSortedReactionsDomain(reactions, usersOwnId)
    )