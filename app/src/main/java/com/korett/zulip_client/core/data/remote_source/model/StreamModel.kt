package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribedStreamsResponse(
    val msg: String,
    val result: String,
    val subscriptions: List<StreamModelNetwork>
)

@Serializable
data class AllStreamsResponse(
    val msg: String,
    val result: String,
    val streams: List<StreamModelNetwork>
)

@Serializable
data class StreamResponse(
    val msg: String,
    val result: String,
    val stream: StreamModelNetwork
)

@Serializable
data class StreamModelNetwork(
    @SerialName("stream_id") val streamId: Int,
    val name: String
)
