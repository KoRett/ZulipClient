package com.korett.zulip_client.core.data.remote_source.model.user_presence

import kotlinx.serialization.Serializable

@Serializable
data class UserPresenceResponse(
    val msg: String,
    val result: String,
    val presence: UserPresenceModelNetwork
)

@Serializable
data class UserPresenceModelNetwork(
    val aggregated: Aggregated
)

@Serializable
data class Aggregated(
    val status: UserStatusNetwork
)