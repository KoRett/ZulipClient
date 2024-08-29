package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state

import com.korett.zulip_client.core.ui.model.StreamModelUi
import com.korett.zulip_client.core.ui.utils.LceState

data class StreamTabState(
    val streams: LceState<List<StreamModelUi>>,
    val isSubscribed: Boolean,
    val lastQuery: String
)