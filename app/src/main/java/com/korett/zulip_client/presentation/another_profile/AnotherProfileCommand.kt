package com.korett.zulip_client.presentation.another_profile

sealed interface AnotherProfileCommand {
    data class LoadAnotherUserData(val userId: Int) : AnotherProfileCommand
}