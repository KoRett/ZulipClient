package com.korett.zulip_client.core.domain.repository

import com.korett.zulip_client.core.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow


interface ChatRepository {
    fun getSortedTopicMessages(
        streamName: String,
        topicName: String
    ): Flow<List<MessageModel>>

    suspend fun getSortedChangedMessages(
        streamName: String,
        topicName: String
    ): Flow<List<MessageModel>>

    suspend fun sentMessageToTopic(streamName: String, topicName: String, content: String)
    suspend fun addReactionToMessage(messageId: Int, emojiCode: String)
    suspend fun removeReactionToMessage(messageId: Int, emojiCode: String)
    suspend fun getSortedPreviousMessages(
        messageId: Int,
        streamName: String,
        topicName: String,
        messagesBefore: Int
    ): List<MessageModel>
}