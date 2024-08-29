package com.korett.zulip_client.presentation.tabs.own_profile

sealed interface OwnProfileCommand {
    data object LoadOwnUserData : OwnProfileCommand
}