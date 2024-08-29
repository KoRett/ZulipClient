package com.korett.zulip_client.core.domain.model

data class TopicModel(
    val name: String,
    val streamId: Int,
    val streamName: String,
    val messageCount: Int,
    val color: Int
)
