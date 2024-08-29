package com.korett.zulip_client.core.data.remote_source.model

import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponse(
    val msg: String,
    val result: String,
    val topics: List<TopicModelNetwork>
)

@Serializable
data class TopicModelNetwork(
    val name: String
)