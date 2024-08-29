package com.korett.zulip_client.presentation.tabs.streams

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class StreamsStoreFactory(private val actor: StreamsActor) {
    fun create(): Store<StreamsEvent, StreamsEffect, Unit> {
        return ElmStore(
            initialState = Unit,
            reducer = StreamsReducer(),
            actor = actor
        )
    }
}