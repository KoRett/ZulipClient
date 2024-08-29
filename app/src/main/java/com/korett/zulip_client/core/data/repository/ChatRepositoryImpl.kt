package com.korett.zulip_client.core.data.repository

import com.korett.zulip_client.core.common.extension.runCatchingNonCancellation
import com.korett.zulip_client.core.data.database.dao.MessageDao
import com.korett.zulip_client.core.data.database.dao.ReactionDao
import com.korett.zulip_client.core.data.database.dao.StreamDao
import com.korett.zulip_client.core.data.database.mapper.toMessageDomain
import com.korett.zulip_client.core.data.database.mapper.toMessageEntity
import com.korett.zulip_client.core.data.database.mapper.toReactionEntity
import com.korett.zulip_client.core.data.local_storage.LocalStorage
import com.korett.zulip_client.core.data.remote_source.ZulipRemoteSource
import com.korett.zulip_client.core.data.remote_source.mapper.toMessageDomain
import com.korett.zulip_client.core.data.remote_source.model.MessageEventModelNetwork
import com.korett.zulip_client.core.data.remote_source.model.MessageModelNetwork
import com.korett.zulip_client.core.domain.model.MessageModel
import com.korett.zulip_client.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val zulipRemoteSource: ZulipRemoteSource,
    private val localStorage: LocalStorage,
    private val messageDao: MessageDao,
    private val reactionDao: ReactionDao,
    private val streamDao: StreamDao
) : ChatRepository {

    @Volatile
    private var queueId: String? = null

    @Volatile
    private var lastEventId: Int? = null

    private suspend fun getUsersOwnId() =
        localStorage.getUsersOwnId() ?: zulipRemoteSource.getOwnData().userId.also {
            localStorage.saveUsersOwnId(it)
        }

    override fun getSortedTopicMessages(
        streamName: String,
        topicName: String
    ): Flow<List<MessageModel>> = flow {
        var messages = messageDao.getTopicMessagesWithReactions(topicName, streamName)
            .map { it.toMessageDomain(streamName, topicName) }
            .sortedBy { it.timestamp }

        if (messages.isNotEmpty()) {
            emit(messages)
        }

        messages = zulipRemoteSource.getTopicNewestMessages(
            ZulipRemoteSource.getNarrowMessageMap(streamName, topicName),
            numBefore = 50
        ).messages
            .map { it.toMessageDomain(getUsersOwnId()) }
            .sortedBy { it.timestamp }

        emit(messages)

        messageDao.deleteTopicMessages(topicName, streamName)
        val streamId = streamDao.getStreamIdByName(streamName)
        if (streamId != null) {
            messages.forEach { message ->
                val messageEntity = message.toMessageEntity(streamId)
                messageDao.saveMessage(messageEntity)

                val reactionEntities = message.reactions.map { it.toReactionEntity(message.id) }
                reactionDao.saveReactions(reactionEntities)
            }
        }
    }

    override suspend fun getSortedChangedMessages(
        streamName: String,
        topicName: String
    ): Flow<List<MessageModel>> = flow {
        deleteMessageEventQueueIfExist()
        registerMessageEventQueue(streamName, topicName)

        while (true) {
            val events = zulipRemoteSource
                .getEvents(queueId!!, lastEventId!!).events
                .sortedBy { it.id }

            lastEventId = events.last().id

            val changedMessages = events.mapNotNull { event ->
                getChangedByEventMessage(event, streamName, topicName)
                    ?.toMessageDomain(getUsersOwnId())
            }.toSet().toList()

            if (changedMessages.isNotEmpty()) {
                emit(changedMessages)
            }
        }
    }.onCompletion { throwable ->
        deleteMessageEventQueueIfExist()
        if (throwable != null) {
            throw throwable
        }
    }

    override suspend fun getSortedPreviousMessages(
        messageId: Int,
        streamName: String,
        topicName: String,
        messagesBefore: Int
    ): List<MessageModel> {
        val messages = zulipRemoteSource.getTopicPreviousMessages(
            messageId,
            ZulipRemoteSource.getNarrowMessageMap(streamName, topicName),
            numBefore = messagesBefore + 1
        ).messages
            .map { it.toMessageDomain(getUsersOwnId()) }
            .sortedBy { it.timestamp }
        return messages.subList(0, messages.lastIndex)
    }

    override suspend fun sentMessageToTopic(
        streamName: String,
        topicName: String,
        content: String
    ) = zulipRemoteSource.sentMessageToTopic(streamName, topicName, content)

    override suspend fun addReactionToMessage(messageId: Int, emojiCode: String) {
        zulipRemoteSource.addReactionToMessage(messageId, getCodepointToNameMap()[emojiCode]!!)
    }

    override suspend fun removeReactionToMessage(messageId: Int, emojiCode: String) {
        zulipRemoteSource.removeReactionToMessage(
            messageId,
            getCodepointToNameMap()[emojiCode]!!
        )
    }

    private suspend fun getChangedByEventMessage(
        event: MessageEventModelNetwork,
        streamName: String,
        topicName: String
    ): MessageModelNetwork? {
        if (event.type == MessageEventModelNetwork.REACTION_TYPE) {
            val message = zulipRemoteSource.getMessageById(event.messageId!!).message

            if (message.displayRecipient == streamName && message.subject == topicName) {
                return message
            }
        } else if (event.type == MessageEventModelNetwork.MESSAGE_TYPE) {
            val message = zulipRemoteSource.getMessageById(event.message!!.id).message

            if (message.displayRecipient == streamName && message.subject == topicName) {
                return message
            }
        }

        return null
    }

    private suspend fun registerMessageEventQueue(streamName: String, topicName: String) {
        val registerResponse = zulipRemoteSource.registerMessageEventQueue(
            ZulipRemoteSource.getNarrowStreamAndTopic(streamName, topicName)
        )

        queueId = registerResponse.queueId
        lastEventId = registerResponse.lastEventId
    }

    private suspend fun deleteMessageEventQueueIfExist() {
        if (queueId != null) {
            runCatchingNonCancellation {
                zulipRemoteSource.deleteEventQueue(queueId!!)
            }.onFailure {
                queueId = null
                lastEventId = null
            }
        }

        queueId = null
        lastEventId = null
    }

    private suspend fun getCodepointToNameMap() = localStorage.getCodepointToNameMap()
        ?: zulipRemoteSource.getEmojisList().codepointToName.also {
            localStorage.saveCodepointToNameMap(it)
        }
}