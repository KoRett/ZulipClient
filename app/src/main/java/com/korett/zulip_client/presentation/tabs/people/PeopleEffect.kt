package com.korett.zulip_client.presentation.tabs.people

sealed interface PeopleEffect {
    data class NavigateToAnotherUserProfile(val userId: Int) : PeopleEffect
}