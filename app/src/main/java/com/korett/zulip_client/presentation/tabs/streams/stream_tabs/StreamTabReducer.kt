package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import com.korett.zulip_client.core.ui.model.StreamModelUi
import com.korett.zulip_client.core.ui.model.TopicModelUi
import com.korett.zulip_client.core.ui.model.TopicState
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state.StreamTabState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class StreamTabReducer : ScreenDslReducer<
        StreamTabEvent,
        StreamTabEvent.Ui,
        StreamTabEvent.Internal,
        StreamTabState,
        StreamTabEffect,
        StreamTabCommand
        >(StreamTabEvent.Ui::class, StreamTabEvent.Internal::class) {

    override fun Result.internal(event: StreamTabEvent.Internal) = when (event) {
        is StreamTabEvent.Internal.StreamsLoadingError -> state {
            copy(streams = LceState.Error(event.throwable))
        }

        is StreamTabEvent.Internal.StreamsLoaded -> {
            when (val streamsState = state.streams) {
                is LceState.Content -> {
                    val currentStreams = compareLoadedStreams(streamsState.data, event.streams)
                    state { copy(streams = LceState.Content(currentStreams)) }
                }

                LceState.Loading -> {
                    state { copy(streams = LceState.Content(event.streams)) }
                }

                else -> Unit
            }
        }

        is StreamTabEvent.Internal.TopicsLoaded -> {
            val currentStreamsState = state.streams
            if (currentStreamsState is LceState.Content) {
                val currentStreams =
                    addTopicsToStream(currentStreamsState.data, event.streamId, event.topics)

                state { copy(streams = LceState.Content(currentStreams)) }
            } else Unit
        }

        is StreamTabEvent.Internal.ErrorSubscribing -> {
            effects { +StreamTabEffect.ErrorSubscribing(event.throwable) }
        }

        is StreamTabEvent.Internal.ErrorUnsubscribing -> {
            effects { +StreamTabEffect.ErrorUnsubscribing(event.throwable) }
        }
    }

    override fun Result.ui(event: StreamTabEvent.Ui): Any = when (event) {
        is StreamTabEvent.Ui.ChangeStreamOpenState -> {
            val currentDelegateItemsState = state.streams
            if (currentDelegateItemsState is LceState.Content) {
                val newStreamDelegateItems = changeStreamOpenState(
                    currentDelegateItemsState.data,
                    event.streamId
                )
                state { copy(streams = LceState.Content(newStreamDelegateItems)) }

                commands { +StreamTabCommand.GetStreamTopics(getOpenedStreams(newStreamDelegateItems)) }
            } else Unit
        }

        StreamTabEvent.Ui.InitStreams -> {
            commands { +StreamTabCommand.SearchStreams(state.isSubscribed, state.lastQuery) }
        }

        is StreamTabEvent.Ui.SearchStreams -> {
            if (state.lastQuery != event.query) {
                commands { +StreamTabCommand.SearchStreams(state.isSubscribed, event.query) }
                state { copy(lastQuery = event.query) }

                val currentDelegateItemsState = state.streams
                if (currentDelegateItemsState is LceState.Content) {
                    val openedStreams = getOpenedStreams(currentDelegateItemsState.data)
                    commands { +StreamTabCommand.GetStreamTopics(openedStreams) }
                } else Unit
            } else Unit
        }

        StreamTabEvent.Ui.RetrySearchStreams -> {
            state { copy(streams = LceState.Loading) }
            commands { +StreamTabCommand.SearchStreams(state.isSubscribed, state.lastQuery) }
        }

        is StreamTabEvent.Ui.SubscribeToStream -> {
            commands { +StreamTabCommand.SubscribeToStream(event.streamName) }
        }

        is StreamTabEvent.Ui.UnsubscribeFromStream -> {
            commands { +StreamTabCommand.UnsubscribeFromStream(event.streamName) }
        }

        is StreamTabEvent.Ui.OpenTopicChat -> {
            effects {
                +StreamTabEffect.OpenTopicChat(event.streamName, event.topicName, event.color)
            }
        }

        is StreamTabEvent.Ui.ShowStreamOptions -> {
            effects {
                if (event.isSubscribed) {
                    +StreamTabEffect.ShowSubscribedOptions(event.streamName)
                } else {
                    +StreamTabEffect.ShowUnsubscribedOptions(event.streamName)
                }
            }
        }
    }

    private fun compareLoadedStreams(
        currentStreams: List<StreamModelUi>,
        newStreams: List<StreamModelUi>
    ): List<StreamModelUi> = newStreams.map { stream ->
        val existedStream = currentStreams.firstOrNull { it.id == stream.id }
        if (existedStream != null) {
            stream.copy(isOpen = existedStream.isOpen, topics = existedStream.topics)
        } else {
            stream
        }
    }

    private fun addTopicsToStream(
        currentStreams: List<StreamModelUi>,
        streamId: Int,
        topics: List<TopicModelUi>
    ): List<StreamModelUi> = currentStreams.map { stream ->
        if (streamId == stream.id) {
            stream.copy(topics = TopicState.Content(topics))
        } else {
            stream
        }
    }

    private fun getOpenedStreams(currentStreams: List<StreamModelUi>): List<StreamModelUi> =
        currentStreams.filter { it.isOpen }

    private fun changeStreamOpenState(
        currentStreams: List<StreamModelUi>,
        streamId: Int
    ): List<StreamModelUi> = currentStreams.map { stream ->
        if (stream.id == streamId) {
            stream.copy(isOpen = !stream.isOpen)
        } else {
            stream
        }
    }
}