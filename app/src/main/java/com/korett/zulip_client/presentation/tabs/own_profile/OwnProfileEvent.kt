package com.korett.zulip_client.presentation.tabs.own_profile

import com.korett.zulip_client.core.ui.model.UserProfileUi

sealed interface OwnProfileEvent {

    sealed interface Ui : OwnProfileEvent {
        data object Init : Ui
    }

    sealed interface Internal : OwnProfileEvent {
        data class OwnDataLoaded(val userProfileUi: UserProfileUi) : Internal
        data object OwnDataLoading : Internal
        data class OwnDataLoadError(val throwable: Throwable) : Internal
    }

}