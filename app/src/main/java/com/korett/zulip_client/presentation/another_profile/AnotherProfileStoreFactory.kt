package com.korett.zulip_client.presentation.another_profile

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.another_profile.state.AnotherProfileState
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

class AnotherProfileStoreFactory (private val actor: AnotherProfileActor) {
    fun create(): Store<AnotherProfileEvent, AnotherProfileEffect, AnotherProfileState> {
        return ElmStore(
            initialState = AnotherProfileState(profile = LceState.Loading),
            reducer = AnotherProfileReducer(),
            actor = actor
        )
    }
}