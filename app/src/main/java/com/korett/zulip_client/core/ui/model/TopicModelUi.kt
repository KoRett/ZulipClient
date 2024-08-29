package com.korett.zulip_client.core.ui.model

data class TopicModelUi (
    val name: String,
    val streamId: Int,
    val streamName: String,
    val messageCount: Int,
    val color: Int
)