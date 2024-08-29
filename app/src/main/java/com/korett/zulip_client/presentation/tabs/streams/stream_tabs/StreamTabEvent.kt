package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import androidx.annotation.ColorInt
import com.korett.zulip_client.core.ui.model.StreamModelUi
import com.korett.zulip_client.core.ui.model.TopicModelUi

sealed interface StreamTabEvent {

    sealed interface Ui : StreamTabEvent {
        data class ChangeStreamOpenState(val streamId: Int, val streamName: String) : Ui
        data object InitStreams : Ui
        data class SearchStreams(val query: String) : Ui
        data object RetrySearchStreams : Ui
        data class SubscribeToStream(val streamName: String) : Ui
        data class UnsubscribeFromStream(val streamName: String) : Ui
        data class OpenTopicChat(
            val streamName: String,
            val topicName: String,
            @ColorInt val color: Int
        ) : Ui

        data class ShowStreamOptions(val isSubscribed: Boolean, val streamName: String) : Ui
    }

    sealed interface Internal : StreamTabEvent {
        data class StreamsLoaded(val streams: List<StreamModelUi>) : Internal
        data class TopicsLoaded(val streamId: Int, val topics: List<TopicModelUi>) : Internal
        data class StreamsLoadingError(val throwable: Throwable) : Internal
        data class ErrorSubscribing(val throwable: Throwable) : Internal
        data class ErrorUnsubscribing(val throwable: Throwable) : Internal
    }

}