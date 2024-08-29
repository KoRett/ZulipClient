package com.korett.zulip_client.presentation.tabs.people

sealed interface PeopleCommand {
    data class SearchPeople(val query: String) : PeopleCommand
}