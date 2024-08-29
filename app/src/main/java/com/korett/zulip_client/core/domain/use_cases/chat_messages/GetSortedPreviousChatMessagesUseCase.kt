package com.korett.zulip_client.core.domain.use_cases.chat_messages

import com.korett.zulip_client.core.domain.repository.ChatRepository
import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.core.domain.use_cases.chat_messages.mapper.mapMessageModelsToChatMessages
import javax.inject.Inject

class GetSortedPreviousChatMessagesUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(
        messageId: Int,
        streamName: String,
        topicName: String,
        messagesBefore: Int
    ): ChatMessages {
        val messages = chatRepository.getSortedPreviousMessages(
            messageId,
            streamName,
            topicName,
            messagesBefore
        )

        return mapMessageModelsToChatMessages(messages, userRepository.getUsersOwnId())
    }
}