package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageEventResponse(
    val result: String,
    val msg: String,
    val events: List<MessageEventModelNetwork>
)

@Serializable
data class MessageEventModelNetwork(
    val type: String,
    val id: Int,

    val op: String? = null,
    @SerialName("user_id") val userId: Int? = null,
    @SerialName("message_id") val messageId: Int? = null,

    val message: MessageModelNetwork? = null
) {
    companion object {
        const val REACTION_TYPE = "reaction"
        const val PRESENCE_TYPE = "presence"
        const val MESSAGE_TYPE = "message"

        const val REACTION_ADD_OP = "add"
        const val REACTION_REMOVE_OP = "remove"
    }
}