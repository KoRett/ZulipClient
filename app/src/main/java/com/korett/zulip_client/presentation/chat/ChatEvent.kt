package com.korett.zulip_client.presentation.chat

import com.korett.zulip_client.core.ui.model.ChatMessagesUi

sealed interface ChatEvent {

    sealed interface Ui : ChatEvent {
        data object Init : Ui
        data object LoadPreviousMessages : Ui
        data object RetryLoadPreviousMessages : Ui
        data class SentMessage(val content: String) : Ui
        data class SelectMessageToAddReaction(val messageId: Int) : Ui
        data class ReactionSelected(val reactionCode: String?) : Ui
        data class ReactionClicked(
            val messageId: Int,
            val reactionCode: String,
            val isSelected: Boolean
        ) : Ui

        data object NavigateBack : Ui
        data class ShowMessageOptions(val messageId: Int, val messageContent: String) : Ui
    }

    sealed interface Internal : ChatEvent {
        data object ChatLoading : Internal
        data class ChatError(val throwable: Throwable) : Internal
        data class ChatLoaded(val chatMessages: ChatMessagesUi) : Internal
        data class ChangedMessages(val changedMessages: ChatMessagesUi) : Internal
        data class PreviousMessagesLoaded(val previousChatMessages: ChatMessagesUi) : Internal
        data class PreviousMessagesLoadError(val throwable: Throwable) : Internal
        data class ErrorSendingMessage(val throwable: Throwable) : Internal
        data object SuccessSendingMessage : Internal
        data class ErrorAddingReaction(val throwable: Throwable) : Internal
        data class ErrorRemovingReaction(val throwable: Throwable) : Internal
    }

}