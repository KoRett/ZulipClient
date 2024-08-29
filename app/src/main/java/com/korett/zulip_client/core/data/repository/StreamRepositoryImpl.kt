package com.korett.zulip_client.core.data.repository

import com.korett.zulip_client.core.data.database.dao.StreamDao
import com.korett.zulip_client.core.data.database.dao.TopicDao
import com.korett.zulip_client.core.data.database.mapper.toStreamDomain
import com.korett.zulip_client.core.data.database.mapper.toStreamEntity
import com.korett.zulip_client.core.data.database.mapper.toTopicDomain
import com.korett.zulip_client.core.data.database.mapper.toTopicEntity
import com.korett.zulip_client.core.data.remote_source.ZulipRemoteSource
import com.korett.zulip_client.core.data.remote_source.mapper.toStreamDomain
import com.korett.zulip_client.core.data.remote_source.mapper.toTopicDomain
import com.korett.zulip_client.core.data.remote_source.model.TopicModelNetwork
import com.korett.zulip_client.core.domain.model.StreamModel
import com.korett.zulip_client.core.domain.model.TopicModel
import com.korett.zulip_client.core.domain.repository.StreamRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StreamRepositoryImpl @Inject constructor(
    private val zulipRemoteSource: ZulipRemoteSource,
    private val streamDAO: StreamDao,
    private val topicDao: TopicDao
) : StreamRepository {

    override fun getAllStreams(): Flow<List<StreamModel>> = flow {
        var allStreams = streamDAO.getAllStreams().map { it.toStreamDomain() }

        if (allStreams.isNotEmpty()) {
            emit(allStreams)
        }

        while (true) {
            allStreams = getAllStreamsRemote()
            emit(allStreams)

            saveAllStreams(allStreams)
            delay(DELAY_MILLIS)
        }
    }

    override fun getSubscribedStreams(): Flow<List<StreamModel>> = flow {
        var subscribedStreams = streamDAO.getSubscribedStreams().map { it.toStreamDomain() }

        if (subscribedStreams.isNotEmpty()) {
            emit(subscribedStreams)
        }

        while (true) {
            subscribedStreams = getSubscribedStreamsRemote()
            emit(subscribedStreams)

            saveAllStreams(getAllStreamsRemote())
            delay(DELAY_MILLIS)
        }
    }

    private suspend fun getSubscribedStreamsRemote(): List<StreamModel> =
        zulipRemoteSource.getSubscribedStreams().subscriptions.map { it.toStreamDomain(true) }

    private suspend fun getAllStreamsRemote(): List<StreamModel> {
        val subscribedStreamIds = getSubscribedStreamsRemote().map { it.id }
        return zulipRemoteSource.getAllStreams().streams.map {
            it.toStreamDomain(isSubscribed = it.streamId in subscribedStreamIds)
        }
    }

    private suspend fun saveAllStreams(allStreams: List<StreamModel>) {
        val streamEntities = allStreams.map { it.toStreamEntity() }
        streamDAO.saveStreams(streamEntities)
    }

    override suspend fun getStreamTopics(
        streamModels: List<StreamModel>
    ): Flow<List<TopicModel>> = flow {
        var topics: List<TopicModel>
        streamModels.forEach { stream ->
            topics = topicDao.getStreamTopics(stream.name).map { it.toTopicDomain(stream.name) }

            if (topics.isNotEmpty()) {
                emit(topics)
            }
        }

        while (true) {
            streamModels.forEach { stream ->
                topics = zulipRemoteSource.getStreamTopics(stream.id).topics.map { topic ->
                    getTopicWithUnreadMessages(topic, stream.id, stream.name)
                }.awaitAll()

                emit(topics)

                topicDao.saveTopics(topics.map { it.toTopicEntity() })
            }
            delay(DELAY_MILLIS)
        }
    }


    private suspend fun getTopicWithUnreadMessages(
        topic: TopicModelNetwork,
        streamId: Int,
        streamName: String
    ) = coroutineScope {
        async {
            val narrow = ZulipRemoteSource.getNarrowMessageMap(streamName, topic.name)
            val unreadMessages = zulipRemoteSource.getTopicUnreadMessages(narrow)
            topic.toTopicDomain(streamId, streamName, unreadMessages.messages.size)
        }
    }

    override suspend fun getStreamById(streamId: Int): StreamModel {
        val subscribedStreamIds = getSubscribedStreamsRemote().map { it.id }
        val stream = zulipRemoteSource.getStreamById(streamId).stream
        return stream.toStreamDomain(stream.streamId in subscribedStreamIds)
    }

    override suspend fun subscribeToStream(streamName: String) =
        zulipRemoteSource.subscribeToStream(ZulipRemoteSource.getSubscribeSubscriptions(streamName))

    override suspend fun unsubscribeFromStream(streamName: String) =
        zulipRemoteSource.unsubscribeToStream(
            ZulipRemoteSource.getUnsubscribeSubscriptions(streamName)
        )

    override suspend fun createStream(streamName: String) =
        zulipRemoteSource.createStream(ZulipRemoteSource.getSubscribeSubscriptions(streamName))

    companion object {
        private const val DELAY_MILLIS = 10000L
    }
}