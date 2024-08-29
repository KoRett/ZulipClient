package com.korett.zulip_client.presentation.chat

import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.core.ui.model.ChatMessagesUi
import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.chat.state.ChatState
import com.korett.zulip_client.presentation.chat.state.PreviousMessagesState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class ChatReducer : ScreenDslReducer<
        ChatEvent,
        ChatEvent.Ui,
        ChatEvent.Internal,
        ChatState,
        ChatEffect,
        ChatCommand
        >(ChatEvent.Ui::class, ChatEvent.Internal::class) {

    override fun Result.internal(event: ChatEvent.Internal): Any = when (event) {
        is ChatEvent.Internal.ErrorAddingReaction -> effects {
            +ChatEffect.ShowErrorAddingReaction(event.throwable)
        }

        is ChatEvent.Internal.ErrorRemovingReaction -> effects {
            +ChatEffect.ShowErrorRemovingReaction(event.throwable)
        }

        is ChatEvent.Internal.ErrorSendingMessage -> effects {
            +ChatEffect.ShowErrorSendingMessage(event.throwable)
        }

        is ChatEvent.Internal.ChatError -> state {
            copy(chatMessagesState = LceState.Error(event.throwable))
        }

        is ChatEvent.Internal.ChatLoaded -> {
            val previousMessagesState =
                if (getChatMessagesSize(event.chatMessages) >= state.initialMessageCount) {
                    PreviousMessagesState.Waiting
                } else {
                    PreviousMessagesState.End
                }

            state {
                copy(
                    chatMessagesState = LceState.Content(event.chatMessages),
                    previousMessagesState = previousMessagesState
                )
            }
        }

        ChatEvent.Internal.ChatLoading -> state {
            copy(chatMessagesState = LceState.Loading)
        }

        ChatEvent.Internal.SuccessSendingMessage -> effects {
            +ChatEffect.ScrollChatToStart
        }

        is ChatEvent.Internal.ChangedMessages -> {
            val chatMessagesState = state.chatMessagesState
            if (chatMessagesState is LceState.Content) {
                val newChatMessages =
                    compareChangedChatMessage(chatMessagesState.data, event.changedMessages)
                state {
                    copy(chatMessagesState = LceState.Content(newChatMessages))
                }
            } else Unit
        }

        is ChatEvent.Internal.PreviousMessagesLoadError -> state {
            copy(previousMessagesState = PreviousMessagesState.Error)
        }

        is ChatEvent.Internal.PreviousMessagesLoaded -> {
            val chatMessagesState = state.chatMessagesState
            if (chatMessagesState is LceState.Content) {
                val newChatMessages =
                    addPreviousChatMessages(chatMessagesState.data, event.previousChatMessages)
                val previousMessagesState =
                    if (getChatMessagesSize(event.previousChatMessages) >= state.previousMessageCount) {
                        PreviousMessagesState.Waiting
                    } else {
                        PreviousMessagesState.End
                    }
                state {
                    copy(
                        chatMessagesState = LceState.Content(newChatMessages),
                        previousMessagesState = previousMessagesState
                    )
                }
            } else Unit
        }
    }

    override fun Result.ui(event: ChatEvent.Ui): Any = when (event) {

        is ChatEvent.Ui.Init -> commands {
            +ChatCommand.InitChat(state.streamName, state.topicName)
        }

        is ChatEvent.Ui.SentMessage -> commands {
            +ChatCommand.SentMessage(state.streamName, state.topicName, event.content)
        }

        is ChatEvent.Ui.SelectMessageToAddReaction -> {
            state { copy(selectedMessageId = event.messageId) }
            effects { +ChatEffect.ShowReactionSelector }
        }

        is ChatEvent.Ui.ReactionSelected -> {
            if (event.reactionCode != null) {
                commands { +ChatCommand.AddReaction(state.selectedMessageId!!, event.reactionCode) }
            }
            state { copy(selectedMessageId = null) }
        }

        is ChatEvent.Ui.ReactionClicked -> {
            if (event.isSelected) {
                commands { +ChatCommand.RemoveReaction(event.messageId, event.reactionCode) }
            } else {
                commands { +ChatCommand.AddReaction(event.messageId, event.reactionCode) }
            }
        }

        is ChatEvent.Ui.LoadPreviousMessages -> {
            val chatMessageState = state.chatMessagesState
            if (chatMessageState is LceState.Content
                && state.previousMessagesState is PreviousMessagesState.Waiting
            ) {
                state { copy(previousMessagesState = PreviousMessagesState.Loading) }
                commands {
                    +ChatCommand.LoadPreviousMessages(
                        getPreviousMessageId(chatMessageState.data),
                        state.streamName,
                        state.topicName,
                        state.previousMessageCount
                    )
                }
            } else Unit
        }

        is ChatEvent.Ui.RetryLoadPreviousMessages -> {
            val chatMessageState = state.chatMessagesState
            if (chatMessageState is LceState.Content
                && state.previousMessagesState is PreviousMessagesState.Error
            ) {
                state { copy(previousMessagesState = PreviousMessagesState.Loading) }

                commands {
                    +ChatCommand.LoadPreviousMessages(
                        getPreviousMessageId(chatMessageState.data),
                        state.streamName,
                        state.topicName,
                        state.previousMessageCount
                    )
                }
            } else Unit
        }

        is ChatEvent.Ui.ShowMessageOptions -> {
            effects { +ChatEffect.ShowMessageOptions(event.messageId, event.messageContent) }
        }

        ChatEvent.Ui.NavigateBack -> {
            effects { +ChatEffect.NavigateBack }
        }
    }

    private fun getPreviousMessageId(chatMessages: ChatMessagesUi): Int {
        val firstOwnMessage = chatMessages.ownMessages.firstOrNull()
        val firstAnotherMessage = chatMessages.anotherMessages.firstOrNull()

        if (firstOwnMessage == null) {
            return firstAnotherMessage!!.id
        }

        if (firstAnotherMessage == null) {
            return firstOwnMessage.id
        }

        return if (firstOwnMessage.timestamp > firstAnotherMessage.timestamp) {
            firstAnotherMessage.id
        } else {
            firstOwnMessage.id
        }
    }

    private fun getChatMessagesSize(chatMessages: ChatMessagesUi): Int =
        chatMessages.anotherMessages.size + chatMessages.ownMessages.size

    private fun addPreviousChatMessages(
        chatMessages: ChatMessagesUi,
        oldChatMessages: ChatMessagesUi
    ): ChatMessagesUi {
        val newOwnMessages = oldChatMessages.ownMessages + chatMessages.ownMessages
        val newAnotherMessages = oldChatMessages.anotherMessages + chatMessages.anotherMessages
        return ChatMessagesUi(newOwnMessages, newAnotherMessages)
    }

    private fun compareChangedChatMessage(
        chatMessages: ChatMessagesUi,
        changedChatMessages: ChatMessagesUi
    ): ChatMessagesUi {
        val newOwnMessages = compareOwnChangedMessages(
            chatMessages.ownMessages,
            changedChatMessages.ownMessages
        )

        val newAnotherMessages = compareAnotherChangedMessages(
            chatMessages.anotherMessages,
            changedChatMessages.anotherMessages
        )

        return ChatMessagesUi(newOwnMessages, newAnotherMessages)
    }

    private fun compareOwnChangedMessages(
        messages: List<OwnMessageModelUi>,
        changedMessages: List<OwnMessageModelUi>
    ): List<OwnMessageModelUi> {
        val changedMessagesMutable = changedMessages.toMutableList()
        val newMessages = messages.map { existedMessage ->
            val changedMessage = changedMessagesMutable.firstOrNull { it.id == existedMessage.id }
            if (changedMessage != null) {
                changedMessagesMutable.remove(changedMessage)
                changedMessage
            } else {
                existedMessage
            }
        }.toMutableList()

        newMessages.addAll(changedMessagesMutable)

        return newMessages.toList().sortedBy { it.timestamp }
    }

    private fun compareAnotherChangedMessages(
        messages: List<AnotherMessageModelUi>,
        changedMessages: List<AnotherMessageModelUi>
    ): List<AnotherMessageModelUi> {
        val changedMessagesMutable = changedMessages.toMutableList()
        val newMessages = messages.map { existedMessage ->
            val changedMessage = changedMessagesMutable.firstOrNull { it.id == existedMessage.id }
            if (changedMessage != null) {
                changedMessagesMutable.remove(changedMessage)
                changedMessage
            } else {
                existedMessage
            }
        }.toMutableList()

        newMessages.addAll(changedMessagesMutable)

        return newMessages.toList().sortedBy { it.timestamp }
    }
}