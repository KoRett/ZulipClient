package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionModelRemote(
    @SerialName("emoji_code") val emojiCode: String,
    @SerialName("emoji_name") val emojiName: String,
    @SerialName("user_id") val userId: Int
)