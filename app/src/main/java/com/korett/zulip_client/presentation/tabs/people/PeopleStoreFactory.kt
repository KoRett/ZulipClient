package com.korett.zulip_client.presentation.tabs.people

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.people.state.PeopleState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class PeopleStoreFactory(private val actor: PeopleActor) {
    fun create(lastQuery: String): Store<PeopleEvent, PeopleEffect, PeopleState> {
        return ElmStore(
            initialState = PeopleState(users = LceState.Loading, lastQuery = lastQuery),
            reducer = PeopleReducer(),
            actor = actor
        )
    }
}