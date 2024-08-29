package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersResponse(
    val msg: String,
    val result: String,
    val members: List<AnotherUserModelNetwork>
)

@Serializable
data class AnotherUserResponse(
    val msg: String,
    val result: String,
    val user: AnotherUserModelNetwork
)

@Serializable
data class AnotherUserModelNetwork(
    @SerialName("user_id") val userId: Int,
    @SerialName("delivery_email") val deliveryEmail: String?,
    val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("avatar_url") val avatarUrl: String?,
    @SerialName("is_bot") val isBot: Boolean,
    @SerialName("is_active") val isActive: Boolean
)