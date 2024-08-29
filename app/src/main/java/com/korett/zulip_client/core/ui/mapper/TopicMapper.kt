package com.korett.zulip_client.core.ui.mapper

import com.korett.zulip_client.core.domain.model.TopicModel
import com.korett.zulip_client.core.ui.model.TopicModelUi

fun TopicModel.toTopicUi(): TopicModelUi = TopicModelUi(
    name = name,
    streamId = streamId,
    streamName = streamName,
    messageCount = messageCount,
    color = color
)