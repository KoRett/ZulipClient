package com.korett.zulip_client.core.ui.model

data class AnotherMessageModelUi(
    val id: Int,
    val senderId: Int,
    val avatarUrl: String?,
    val senderFullName: String,
    val content: String,
    val timestamp: Long,
    val streamName: String,
    val topicName: String,
    val reactions: List<ReactionModelUi>
)