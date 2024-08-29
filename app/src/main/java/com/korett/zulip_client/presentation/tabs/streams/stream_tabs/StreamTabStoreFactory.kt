package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state.StreamTabState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class StreamTabStoreFactory(private val actor: StreamTabActor) {
    fun create(isSubscribed: Boolean, lastQuery: String): Store<StreamTabEvent, StreamTabEffect, StreamTabState> {
        return ElmStore(
            initialState = StreamTabState(streams = LceState.Loading, isSubscribed, lastQuery),
            reducer = StreamTabReducer(),
            actor = actor
        )
    }
}