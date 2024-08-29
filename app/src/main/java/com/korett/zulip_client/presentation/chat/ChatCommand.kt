package com.korett.zulip_client.presentation.chat

sealed interface ChatCommand {
    data class InitChat(
        val streamName: String,
        val topicName: String
    ) : ChatCommand

    data class SentMessage(
        val streamName: String,
        val topicName: String,
        val content: String
    ) : ChatCommand

    data class AddReaction(val messageId: Int, val reactionCode: String) : ChatCommand
    data class RemoveReaction(val messageId: Int, val reactionCode: String) : ChatCommand
    data class LoadPreviousMessages(
        val messageId: Int,
        val streamName: String,
        val topicName: String,
        val messageCount: Int
    ) : ChatCommand
}