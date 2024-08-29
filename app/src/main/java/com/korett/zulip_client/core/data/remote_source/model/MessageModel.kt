package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponse(
    val msg: String,
    val result: String,
    val messages: List<MessageModelNetwork>
)

@Serializable
data class MessageResponse(
    val msg: String,
    val result: String,
    val message: MessageModelNetwork
)

@Serializable
data class MessageModelNetwork(
    val id: Int,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("sender_id") val senderId: Int,
    @SerialName("sender_full_name") val senderFullName: String,
    @SerialName("display_recipient") val displayRecipient: String,
    val subject: String,
    val content: String,
    val timestamp: Long,
    val reactions: List<ReactionModelRemote>
)