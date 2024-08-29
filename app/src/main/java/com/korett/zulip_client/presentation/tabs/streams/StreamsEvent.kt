package com.korett.zulip_client.presentation.tabs.streams

sealed interface StreamsEvent {
    sealed interface Ui : StreamsEvent {
        data class CreateStream(val streamName: String?) : Ui
        data object ShowOptions : Ui
        data object ShowStreamCreation : Ui
    }

    sealed interface Internal : StreamsEvent {
        data class StreamCreationError(val throwable: Throwable) : Internal
        data object StreamAlreadyExist : Internal
    }
}