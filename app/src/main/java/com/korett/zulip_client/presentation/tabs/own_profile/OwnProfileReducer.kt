package com.korett.zulip_client.presentation.tabs.own_profile

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.own_profile.state.OwnProfileState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class OwnProfileReducer :
    ScreenDslReducer<OwnProfileEvent,
            OwnProfileEvent.Ui,
            OwnProfileEvent.Internal,
            OwnProfileState,
            OwnProfileEffect,
            OwnProfileCommand>(OwnProfileEvent.Ui::class, OwnProfileEvent.Internal::class) {

    override fun Result.internal(event: OwnProfileEvent.Internal): Any = when (event) {
        is OwnProfileEvent.Internal.OwnDataLoaded -> state {
            copy(profile = LceState.Content(event.userProfileUi))
        }

        is OwnProfileEvent.Internal.OwnDataLoadError -> state {
            copy(profile = LceState.Error(event.throwable))
        }

        OwnProfileEvent.Internal.OwnDataLoading -> state {
            copy(profile = LceState.Loading)
        }
    }

    override fun Result.ui(event: OwnProfileEvent.Ui): Any = when (event) {
        OwnProfileEvent.Ui.Init -> commands {
            +OwnProfileCommand.LoadOwnUserData
        }
    }
}