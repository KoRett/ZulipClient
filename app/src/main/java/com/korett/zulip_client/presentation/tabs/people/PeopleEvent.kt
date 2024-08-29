package com.korett.zulip_client.presentation.tabs.people

import com.korett.zulip_client.core.ui.model.UserModelUi

interface PeopleEvent {
    sealed interface Ui : PeopleEvent {
        data object InitPeople : Ui
        data class SearchPeople(val query: String) : Ui
        data class RetrySearchPeople(val query: String) : Ui
        data class NavigateToAnotherUserProfile(val userId: Int) : Ui
    }

    sealed interface Internal : PeopleEvent {
        data class UsersLoaded(val users: List<UserModelUi>) : Internal
        data object UsersLoading : Internal
        data class UsersLoadError(val throwable: Throwable) : Internal
    }
}