package com.korett.zulip_client.presentation.tabs.streams

sealed interface StreamsEffect {
    data class StreamCreationError(val throwable: Throwable) : StreamsEffect
    data object StreamAlreadyExist : StreamsEffect
    data object ShowOptions : StreamsEffect
    data object ShowStreamCreation : StreamsEffect
}