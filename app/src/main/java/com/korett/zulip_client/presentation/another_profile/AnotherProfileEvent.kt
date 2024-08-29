package com.korett.zulip_client.presentation.another_profile

import com.korett.zulip_client.core.ui.model.UserProfileUi

sealed interface AnotherProfileEvent {

    sealed interface Ui : AnotherProfileEvent {
        data class Init(val userId: Int) : Ui
    }

    sealed interface Internal : AnotherProfileEvent {
        data class AnotherDataLoaded(val userProfileUi: UserProfileUi) : Internal
        data object AnotherDataLoading : Internal
        data class AnotherDataLoadError(val throwable: Throwable) : Internal
    }

}