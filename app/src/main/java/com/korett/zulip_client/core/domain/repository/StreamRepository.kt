package com.korett.zulip_client.core.domain.repository

import com.korett.zulip_client.core.domain.model.StreamModel
import com.korett.zulip_client.core.domain.model.TopicModel
import kotlinx.coroutines.flow.Flow


interface StreamRepository {
    fun getSubscribedStreams(): Flow<List<StreamModel>>
    fun getAllStreams(): Flow<List<StreamModel>>
    suspend fun getStreamById(streamId: Int): StreamModel
    suspend fun getStreamTopics(streamModels: List<StreamModel>): Flow<List<TopicModel>>
    suspend fun subscribeToStream(streamName: String)
    suspend fun createStream(streamName: String)
    suspend fun unsubscribeFromStream(streamName: String)
}