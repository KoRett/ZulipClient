package com.korett.zulip_client.core.data.database.mapper

import com.korett.zulip_client.core.data.database.entity.MessageEntity
import com.korett.zulip_client.core.data.database.entity.relationship.MessageWithReactions
import com.korett.zulip_client.core.domain.model.MessageModel

fun MessageWithReactions.toMessageDomain(streamName: String, topicName: String): MessageModel =
    MessageModel(
        id = messageEntity.id,
        senderId = messageEntity.senderId,
        avatarUrl = messageEntity.avatarUrl,
        senderFullName = messageEntity.senderFullName,
        content = messageEntity.content,
        timestamp = messageEntity.timestamp,
        streamName = streamName,
        topicName = topicName,
        reactions = reactionEntities.map { it.toReactionDomain() }
    )

fun MessageModel.toMessageEntity(streamId: Int): MessageEntity = MessageEntity(
    id = id,
    senderId = senderId,
    avatarUrl = avatarUrl,
    senderFullName = senderFullName,
    content = content,
    timestamp = timestamp,
    topicName = topicName,
    streamId = streamId
)
