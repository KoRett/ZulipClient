package com.korett.zulip_client.presentation.chat

import com.korett.zulip_client.core.common.extension.runCatchingNonCancellation
import com.korett.zulip_client.core.domain.repository.ChatRepository
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedChangedMessages
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedChatMessagesUseCase
import com.korett.zulip_client.core.domain.use_cases.chat_messages.GetSortedPreviousChatMessagesUseCase
import com.korett.zulip_client.core.ui.mapper.toChatMessagesUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.retry
import okhttp3.internal.toHexString
import vivid.money.elmslie.core.store.Actor

class ChatActor(
    private val chatRepository: ChatRepository,
    private val getSortedChatMessagesUseCase: GetSortedChatMessagesUseCase,
    private val getSortedPreviousChatMessagesUseCase: GetSortedPreviousChatMessagesUseCase,
    private val getSortedChangedChatMessages: GetSortedChangedMessages
) : Actor<ChatCommand, ChatEvent>() {
    override fun execute(command: ChatCommand): Flow<ChatEvent> = when (command) {
        is ChatCommand.InitChat -> initChat(command)

        is ChatCommand.SentMessage -> sendMessageCommand(command)

        is ChatCommand.AddReaction -> addReaction(command)

        is ChatCommand.RemoveReaction -> removeReaction(command)

        is ChatCommand.LoadPreviousMessages -> getPreviousMessages(command)
    }

    private fun initChat(command: ChatCommand.InitChat): Flow<ChatEvent.Internal> = flow {
        runCatchingNonCancellation {
            getSortedChatMessagesUseCase(command.streamName, command.topicName)
                .onCompletion { throwable ->
                    if (throwable == null) {
                        getSortedChangedChatMessages(command.streamName, command.topicName)
                            .retry {
                                delay(RETRY_DELAY_MILLIS)
                                true
                            }
                            .map { it.toChatMessagesUi() }
                            .collect {
                                emit(ChatEvent.Internal.ChangedMessages(it))
                            }
                    }
                }
                .collect { chatMessages ->
                    emit(ChatEvent.Internal.ChatLoaded(chatMessages.toChatMessagesUi()))
                }
        }.onFailure {
            emit(ChatEvent.Internal.ChatError(it))
        }
    }

    private fun getPreviousMessages(command: ChatCommand.LoadPreviousMessages): Flow<ChatEvent.Internal> =
        flow {
            runCatchingNonCancellation {
                val chatMessages = getSortedPreviousChatMessagesUseCase(
                    command.messageId,
                    command.streamName,
                    command.topicName,
                    command.messageCount
                )

                emit(ChatEvent.Internal.PreviousMessagesLoaded(chatMessages.toChatMessagesUi()))
            }.onFailure {
                emit(ChatEvent.Internal.PreviousMessagesLoadError(it))
            }
        }

    private fun sendMessageCommand(command: ChatCommand.SentMessage): Flow<ChatEvent.Internal> =
        flow {
            runCatchingNonCancellation {
                chatRepository.sentMessageToTopic(
                    command.streamName,
                    command.topicName,
                    command.content
                )
            }.fold(
                onSuccess = { emit(ChatEvent.Internal.SuccessSendingMessage) },
                onFailure = { emit(ChatEvent.Internal.ErrorSendingMessage(it)) }
            )
        }

    private fun addReaction(command: ChatCommand.AddReaction): Flow<ChatEvent.Internal> = flow {
        runCatchingNonCancellation {
            chatRepository.addReactionToMessage(
                command.messageId,
                getHexStringReactionCode(command.reactionCode)
            )
        }.onFailure {
            emit(ChatEvent.Internal.ErrorAddingReaction(it))
        }
    }

    private fun removeReaction(command: ChatCommand.RemoveReaction): Flow<ChatEvent.Internal> =
        flow {
            runCatchingNonCancellation {
                chatRepository.removeReactionToMessage(
                    command.messageId,
                    getHexStringReactionCode(command.reactionCode)
                )
            }.onFailure {
                emit(ChatEvent.Internal.ErrorRemovingReaction(it))
            }
        }

    private fun getHexStringReactionCode(reaction: String): String {
        var reactionCode = reaction.codePointAt(0).toHexString()

        for (i in 2..reaction.lastIndex step 2) {
            reactionCode += "-${reaction.codePointAt(i).toHexString()}"
        }

        return reactionCode
    }

    companion object {
        private const val RETRY_DELAY_MILLIS = 2000L
    }
}