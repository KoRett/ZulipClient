package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import com.korett.zulip_client.core.common.extension.runCatchingNonCancellation
import com.korett.zulip_client.core.domain.repository.StreamRepository
import com.korett.zulip_client.core.domain.use_cases.SearchStreamsUseCase
import com.korett.zulip_client.core.ui.mapper.toStreamDomain
import com.korett.zulip_client.core.ui.mapper.toStreamUi
import com.korett.zulip_client.core.ui.mapper.toTopicUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.switcher.Switcher

class StreamTabActor(
    private val streamRepository: StreamRepository,
    private val searchStreamsUseCase: SearchStreamsUseCase
) : Actor<StreamTabCommand, StreamTabEvent>() {

    private val streamSwitcher = Switcher()
    private val topicSwitcher = Switcher()

    override fun execute(command: StreamTabCommand): Flow<StreamTabEvent> {

        return when (command) {
            is StreamTabCommand.SearchStreams -> streamSwitcher.switch(DEBOUNCE_MILLIS) {
                searchStreams(command)
            }

            is StreamTabCommand.GetStreamTopics -> topicSwitcher.switch(DEBOUNCE_MILLIS) {
                getStreamTopics(command)
            }

            is StreamTabCommand.SubscribeToStream -> subscribeToStream(command)

            is StreamTabCommand.UnsubscribeFromStream -> unsubscribeFromStream(command)
        }
    }

    private fun getStreamTopics(
        command: StreamTabCommand.GetStreamTopics
    ): Flow<StreamTabEvent.Internal> =
        flow {
            val streamModels = command.openedStreams.map { it.toStreamDomain() }
            streamRepository.getStreamTopics(streamModels)
                .map { it.map { topic -> topic.toTopicUi() } }
                .catch { emit(StreamTabEvent.Internal.StreamsLoadingError(it)) }
                .collect { topicDelegateItems ->
                    val streamId = topicDelegateItems[0].streamId
                    emit(StreamTabEvent.Internal.TopicsLoaded(streamId, topicDelegateItems))
                }
        }

    private fun searchStreams(command: StreamTabCommand.SearchStreams): Flow<StreamTabEvent.Internal> =
        flow {
            if (command.query.isNotBlank()) {
                searchStreamsUseCase(command.isSubscribed, command.query)
            } else {
                getStreams(command.isSubscribed)
            }
                .map { it.map { streamModel -> streamModel.toStreamUi() } }
                .map { it.sortedBy { streamModelUi -> streamModelUi.name } }
                .catch { emit(StreamTabEvent.Internal.StreamsLoadingError(it)) }
                .collect { emit(StreamTabEvent.Internal.StreamsLoaded(it)) }
        }

    private fun subscribeToStream(command: StreamTabCommand.SubscribeToStream): Flow<StreamTabEvent.Internal> =
        flow {
            runCatchingNonCancellation {
                streamRepository.subscribeToStream(command.streamName)
            }.onFailure {
                emit(StreamTabEvent.Internal.ErrorSubscribing(it))
            }
        }

    private fun unsubscribeFromStream(command: StreamTabCommand.UnsubscribeFromStream): Flow<StreamTabEvent.Internal> =
        flow {
            runCatchingNonCancellation {
                streamRepository.unsubscribeFromStream(command.streamName)
            }.onFailure {
                emit(StreamTabEvent.Internal.ErrorUnsubscribing(it))
            }
        }

    private fun getStreams(isSubscribed: Boolean) = if (isSubscribed) {
        streamRepository.getSubscribedStreams()
    } else {
        streamRepository.getAllStreams()
    }

    companion object {
        private const val DEBOUNCE_MILLIS = 500L
    }
}