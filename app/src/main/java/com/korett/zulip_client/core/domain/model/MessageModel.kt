package com.korett.zulip_client.core.domain.model

data class MessageModel(
    val id: Int,
    val senderId: Int,
    val avatarUrl: String?,
    val senderFullName: String,
    val content: String,
    val timestamp: Long,
    val streamName: String,
    val topicName: String,
    val reactions: List<ReactionModel>
)