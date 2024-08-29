package com.korett.zulip_client.presentation.tabs.streams

import com.korett.zulip_client.core.common.extension.runCatchingNonCancellation
import com.korett.zulip_client.core.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import vivid.money.elmslie.core.store.Actor

class StreamsActor(
    private val streamRepository: StreamRepository
) : Actor<StreamsCommand, StreamsEvent>() {

    override fun execute(command: StreamsCommand): Flow<StreamsEvent> = when (command) {
        is StreamsCommand.CreateStream -> createStream(command)
    }

    private fun createStream(command: StreamsCommand.CreateStream): Flow<StreamsEvent.Internal> =
        flow {
            runCatchingNonCancellation {
                val allStreamNames = streamRepository.getAllStreams().take(2).last().map { it.name }
                if (command.streamName !in allStreamNames) {
                    streamRepository.createStream(command.streamName)
                } else {
                    emit(StreamsEvent.Internal.StreamAlreadyExist)
                }
            }.onFailure {
                emit(StreamsEvent.Internal.StreamCreationError(it))
            }
        }
}