package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class OwnUserDataResponse(
    val msg: String,
    val result: String,
    @SerialName("user_id") val userId: Int,
    @SerialName("delivery_email") val deliveryEmail: String?,
    val email: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("avatar_url") val avatarUrl: String?
)