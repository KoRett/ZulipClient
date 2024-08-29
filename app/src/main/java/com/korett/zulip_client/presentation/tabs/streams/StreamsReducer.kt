package com.korett.zulip_client.presentation.tabs.streams

import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class StreamsReducer : ScreenDslReducer<
        StreamsEvent,
        StreamsEvent.Ui,
        StreamsEvent.Internal,
        Unit,
        StreamsEffect,
        StreamsCommand
        >(StreamsEvent.Ui::class, StreamsEvent.Internal::class) {
    override fun Result.internal(event: StreamsEvent.Internal) = when (event) {
        is StreamsEvent.Internal.StreamCreationError -> effects {
            +StreamsEffect.StreamCreationError(event.throwable)
        }

        StreamsEvent.Internal.StreamAlreadyExist -> effects { +StreamsEffect.StreamAlreadyExist }
    }

    override fun Result.ui(event: StreamsEvent.Ui) = when (event) {
        is StreamsEvent.Ui.CreateStream -> commands {
            if (event.streamName != null) {
                commands { +StreamsCommand.CreateStream(event.streamName) }
            } else Unit
        }

        StreamsEvent.Ui.ShowOptions -> {
            effects { +StreamsEffect.ShowOptions }
        }

        StreamsEvent.Ui.ShowStreamCreation -> {
            effects { +StreamsEffect.ShowStreamCreation }
        }
    }
}