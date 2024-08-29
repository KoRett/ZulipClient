package com.korett.zulip_client.core.domain.use_cases.chat_messages

import com.korett.zulip_client.core.domain.repository.ChatRepository
import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.core.domain.use_cases.chat_messages.mapper.mapMessageModelsToChatMessages
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSortedChangedMessages @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(streamName: String, topicName: String): Flow<ChatMessages> =
        chatRepository.getSortedChangedMessages(streamName, topicName).map { messages ->
            mapMessageModelsToChatMessages(messages, userRepository.getUsersOwnId())
        }
}