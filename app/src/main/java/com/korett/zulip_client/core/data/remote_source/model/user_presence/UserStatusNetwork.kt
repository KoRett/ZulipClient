package com.korett.zulip_client.core.data.remote_source.model.user_presence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserStatusNetwork(val state: String) {
    @SerialName("active")
    ACTIVE("active"),

    @SerialName("idle")
    IDLE("idle"),

    @SerialName("offline")
    OFFLINE("offline")
}