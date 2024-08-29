package com.korett.zulip_client.presentation.chat.test_data

import com.korett.zulip_client.core.domain.model.MessageModel
import com.korett.zulip_client.core.domain.model.ReactionModel
import com.korett.zulip_client.core.domain.model.UserModel
import com.korett.zulip_client.core.domain.repository.ChatRepository
import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedChangedMessages
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedChatMessagesUseCase
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedPreviousChatMessagesUseCase
import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.core.ui.model.ChatMessagesUi
import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.model.ReactionModelUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class ChatActorTestData {

    val streamName = "stream_name"
    val topicName = "topic_name"
    private val senderFullName = "some_name"
    val messageContent = "some_text"

    val messageLoadCount = 20

    val messageId = 1
    val reaction = "1F601"

    private val usersOwnId = 1
    private val usersAnotherId = 2

    val loadError = IOException()

    val messages = listOf(
        MessageModel(
            id = 1,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780827,
            streamName = streamName,
            topicName = topicName,
            reactions = listOf(
                ReactionModel(reactionCode = "1f601", count = 1, isSelected = true)
            )
        ),
        MessageModel(
            id = 2,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780828,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        ),
        MessageModel(
            id = 3,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780829,
            streamName = streamName,
            topicName = topicName,
            reactions = listOf()
        ),
        MessageModel(
            id = 4,
            senderId = usersAnotherId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780830,
            streamName = streamName,
            topicName = topicName,
            reactions = listOf()
        ),
        MessageModel(
            id = 5,
            senderId = usersAnotherId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780831,
            streamName = streamName,
            topicName = topicName,
            reactions = listOf(
                ReactionModel(reactionCode = "1f601", count = 3, isSelected = false)
            )
        ),
        MessageModel(
            id = 6,
            senderId = usersAnotherId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780832,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        )
    )

    val changedMessages = listOf(
        MessageModel(
            id = 3,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780829,
            streamName = streamName,
            topicName = topicName,
            reactions = listOf(
                ReactionModel(
                    reactionCode = "1f601",
                    count = 1,
                    isSelected = false
                )
            )
        ),
        MessageModel(
            id = 6,
            senderId = usersAnotherId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780832,
            streamName = streamName,
            topicName = topicName,
            reactions = listOf(
                ReactionModel(
                    reactionCode = "1f601",
                    count = 1,
                    isSelected = false
                )
            )
        )
    )

    val manyPreviousMessages = listOf(
        MessageModel(
            id = 7,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780821,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        ),
        MessageModel(
            id = 8,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780822,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        ),
        MessageModel(
            id = 9,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780823,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        ), MessageModel(
            id = 10,
            senderId = usersAnotherId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780824,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        ),
        MessageModel(
            id = 11,
            senderId = usersAnotherId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780825,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        ),
        MessageModel(
            id = 12,
            senderId = usersAnotherId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780826,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        )
    )

    val fewPreviousMessages = listOf(
        MessageModel(
            id = 12,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780819,
            streamName = streamName,
            topicName = topicName,
            reactions = listOf()
        ),
        MessageModel(
            id = 13,
            senderId = usersOwnId,
            avatarUrl = null,
            senderFullName = senderFullName,
            content = messageContent,
            timestamp = 1715780820,
            streamName = streamName,
            topicName = topicName,
            reactions = emptyList()
        )
    )

    val chatMessages =
        ChatMessagesUi(
            ownMessages = listOf(
                OwnMessageModelUi(
                    id = 1,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780827,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = true)
                    )
                ),
                OwnMessageModelUi(
                    id = 2,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780828,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                OwnMessageModelUi(
                    id = 3,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780829,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf()
                )
            ),
            anotherMessages = listOf(
                AnotherMessageModelUi(
                    id = 4,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780830,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf()
                ),
                AnotherMessageModelUi(
                    id = 5,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780831,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "1f601", count = 3, isSelected = false)
                    )
                ),
                AnotherMessageModelUi(
                    id = 6,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780832,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            )
        )

    val changedChatMessages =
        ChatMessagesUi(
            ownMessages = listOf(
                OwnMessageModelUi(
                    id = 3,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780829,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = false)
                    )
                )
            ),
            anotherMessages = listOf(
                AnotherMessageModelUi(
                    id = 6,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780832,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = false)
                    )
                )
            )
        )

    val expectedPreviousChatItems =
        ChatMessagesUi(
            ownMessages = listOf(
                OwnMessageModelUi(
                    id = 7,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780821,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                OwnMessageModelUi(
                    id = 8,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780822,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                OwnMessageModelUi(
                    id = 9,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780823,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            ),
            anotherMessages = listOf(
                AnotherMessageModelUi(
                    id = 10,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780824,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                AnotherMessageModelUi(
                    id = 11,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780825,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                AnotherMessageModelUi(
                    id = 12,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780826,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            )
        )

    val successChatRepository = object : ChatRepository {
        override fun getSortedTopicMessages(
            streamName: String,
            topicName: String
        ): Flow<List<MessageModel>> = flow { emit(messages) }

        override suspend fun getSortedChangedMessages(
            streamName: String,
            topicName: String
        ): Flow<List<MessageModel>> = flow { emit(changedMessages) }

        override suspend fun sentMessageToTopic(
            streamName: String,
            topicName: String,
            content: String
        ) = Unit

        override suspend fun addReactionToMessage(messageId: Int, emojiCode: String) = Unit

        override suspend fun removeReactionToMessage(messageId: Int, emojiCode: String) = Unit

        override suspend fun getSortedPreviousMessages(
            messageId: Int,
            streamName: String,
            topicName: String,
            messagesBefore: Int
        ): List<MessageModel> = manyPreviousMessages
    }

    val errorChatRepository = object : ChatRepository {
        override fun getSortedTopicMessages(
            streamName: String,
            topicName: String
        ): Flow<List<MessageModel>> = flow {
            throw loadError
        }

        override suspend fun getSortedChangedMessages(
            streamName: String,
            topicName: String
        ): Flow<List<MessageModel>> = flow {
            throw loadError
        }

        override suspend fun sentMessageToTopic(
            streamName: String,
            topicName: String,
            content: String
        ) {
            throw loadError
        }

        override suspend fun addReactionToMessage(messageId: Int, emojiCode: String) {
            throw loadError
        }

        override suspend fun removeReactionToMessage(messageId: Int, emojiCode: String) {
            throw loadError
        }

        override suspend fun getSortedPreviousMessages(
            messageId: Int,
            streamName: String,
            topicName: String,
            messagesBefore: Int
        ): List<MessageModel> {
            throw loadError
        }
    }

    private val userRepository = object : UserRepository {
        override fun getAllUsers(): Flow<List<UserModel>> {
            throw loadError
        }

        override suspend fun getUserDataById(userId: Int): UserModel {
            throw loadError
        }

        override suspend fun getOwnUserData(): UserModel {
            throw loadError
        }

        override suspend fun getUsersOwnId(): Int = usersOwnId
    }

    val successGetSortedChatMessagesUseCase =
        GetSortedChatMessagesUseCase(userRepository, successChatRepository)
    val successGetSortedPreviousChatMessagesUseCase =
        GetSortedPreviousChatMessagesUseCase(userRepository, successChatRepository)
    val successGetSortedChangedMessages =
        GetSortedChangedMessages(userRepository, successChatRepository)

    val errorGetSortedChatMessagesUseCase =
        GetSortedChatMessagesUseCase(userRepository, errorChatRepository)
    val errorGetSortedPreviousChatMessagesUseCase =
        GetSortedPreviousChatMessagesUseCase(userRepository, errorChatRepository)
    val errorGetSortedChangedMessages =
        GetSortedChangedMessages(userRepository, errorChatRepository)

}