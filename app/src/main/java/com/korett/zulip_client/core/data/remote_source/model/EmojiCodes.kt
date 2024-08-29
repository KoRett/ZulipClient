package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmojiCodesModelNetwork(
    @SerialName("codepoint_to_name") val codepointToName: Map<String, String>
)