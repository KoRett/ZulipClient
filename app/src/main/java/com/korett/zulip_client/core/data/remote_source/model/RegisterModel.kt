package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterEventQueueResponse(
    val result: String,
    val msg: String,
    @SerialName("queue_id") val queueId: String,
    @SerialName("last_event_id") val lastEventId: Int
)