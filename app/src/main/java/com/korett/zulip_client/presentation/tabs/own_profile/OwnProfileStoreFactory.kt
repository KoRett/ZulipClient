package com.korett.zulip_client.presentation.tabs.own_profile

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.own_profile.state.OwnProfileState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class OwnProfileStoreFactory (private val actor: OwnProfileActor) {
    fun create(): Store<OwnProfileEvent, OwnProfileEffect, OwnProfileState> {
        return ElmStore(
            initialState = OwnProfileState(profile = LceState.Loading),
            reducer = OwnProfileReducer(),
            actor = actor
        )
    }
}