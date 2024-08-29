package com.korett.zulip_client.core.data.database.mapper

import com.korett.zulip_client.core.data.database.entity.TopicEntity
import com.korett.zulip_client.core.domain.model.TopicModel

fun TopicEntity.toTopicDomain(streamName: String): TopicModel = TopicModel(
    name = name,
    streamId = streamId,
    streamName = streamName,
    messageCount = messageCount,
    color = color
)

fun TopicModel.toTopicEntity(): TopicEntity = TopicEntity(
    name = name,
    streamId = streamId,
    messageCount = messageCount,
    color = color
)