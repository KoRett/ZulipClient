package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import com.korett.zulip_client.core.ui.model.StreamModelUi

sealed interface StreamTabCommand {
    data class SearchStreams(val isSubscribed: Boolean, val query: String) : StreamTabCommand
    data class GetStreamTopics(val openedStreams: List<StreamModelUi>) : StreamTabCommand
    data class SubscribeToStream(val streamName: String) : StreamTabCommand
    data class UnsubscribeFromStream(val streamName: String) : StreamTabCommand
}