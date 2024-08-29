package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import androidx.annotation.ColorInt

sealed interface StreamTabEffect {
    data class ErrorSubscribing(val throwable: Throwable) : StreamTabEffect
    data class ErrorUnsubscribing(val throwable: Throwable) : StreamTabEffect
    data class OpenTopicChat(
        val streamName: String,
        val topicName: String,
        @ColorInt val color: Int
    ) : StreamTabEffect

    data class ShowSubscribedOptions(val streamName: String) : StreamTabEffect
    data class ShowUnsubscribedOptions(val streamName: String) : StreamTabEffect
}