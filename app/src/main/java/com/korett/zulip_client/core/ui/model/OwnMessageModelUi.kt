package com.korett.zulip_client.core.ui.model

data class OwnMessageModelUi(
    val id: Int,
    val senderId: Int,
    val content: String,
    val timestamp: Long,
    val streamName: String,
    val topicName: String,
    val reactions: List<ReactionModelUi>
)