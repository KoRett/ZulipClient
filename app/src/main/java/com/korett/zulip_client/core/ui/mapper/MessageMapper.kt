package com.korett.zulip_client.core.ui.mapper

import com.korett.zulip_client.core.domain.model.MessageModel
import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.core.ui.model.OwnMessageModelUi

fun MessageModel.toOwnMessageUi(): OwnMessageModelUi = OwnMessageModelUi(
    id = id,
    senderId = senderId,
    content = content,
    timestamp = timestamp,
    streamName = streamName,
    topicName = topicName,
    reactions = reactions.map { it.toReactionUi() }
)

fun MessageModel.toAnotherMessageUi(): AnotherMessageModelUi = AnotherMessageModelUi(
    id = id,
    senderId = senderId,
    avatarUrl = avatarUrl,
    senderFullName = senderFullName,
    content = content,
    timestamp = timestamp,
    streamName = streamName,
    topicName = topicName,
    reactions = reactions.map { it.toReactionUi() }
)