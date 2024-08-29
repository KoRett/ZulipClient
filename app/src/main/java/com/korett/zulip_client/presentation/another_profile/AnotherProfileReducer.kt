package com.korett.zulip_client.presentation.another_profile

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.another_profile.state.AnotherProfileState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class AnotherProfileReducer :
    ScreenDslReducer<AnotherProfileEvent,
            AnotherProfileEvent.Ui,
            AnotherProfileEvent.Internal,
            AnotherProfileState,
            AnotherProfileEffect,
            AnotherProfileCommand>(
        AnotherProfileEvent.Ui::class, AnotherProfileEvent.Internal::class
    ) {

    override fun Result.internal(event: AnotherProfileEvent.Internal): Any = when (event) {
        is AnotherProfileEvent.Internal.AnotherDataLoaded -> state {
            copy(profile = LceState.Content(event.userProfileUi))
        }

        is AnotherProfileEvent.Internal.AnotherDataLoadError -> state {
            copy(profile = LceState.Error(event.throwable))
        }

        AnotherProfileEvent.Internal.AnotherDataLoading -> state {
            copy(profile = LceState.Loading)
        }
    }

    override fun Result.ui(event: AnotherProfileEvent.Ui): Any = when (event) {
        is AnotherProfileEvent.Ui.Init -> commands {
            +AnotherProfileCommand.LoadAnotherUserData(event.userId)
        }
    }
}