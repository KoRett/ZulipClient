package com.korett.zulip_client.core.ui.model

data class StreamModelUi(
    val id: Int,
    val name: String,
    val isOpen: Boolean,
    val topics: TopicState,
    val isSubscribed: Boolean
)

sealed class TopicState {
    data object Loading : TopicState()
    data class Content(val data: List<TopicModelUi>) : TopicState()
}