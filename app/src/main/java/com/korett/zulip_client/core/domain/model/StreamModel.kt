package com.korett.zulip_client.core.domain.model

data class StreamModel(
    val id: Int,
    val name: String,
    val isSubscribed: Boolean
)
