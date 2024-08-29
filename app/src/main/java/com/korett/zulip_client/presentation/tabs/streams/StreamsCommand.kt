package com.korett.zulip_client.presentation.tabs.streams

sealed interface StreamsCommand {
    data class CreateStream(val streamName: String) : StreamsCommand
}