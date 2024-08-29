package com.korett.zulip_client.presentation.chat

sealed interface ChatEffect {
    data class ShowErrorSendingMessage(val throwable: Throwable) : ChatEffect
    data class ShowErrorAddingReaction(val throwable: Throwable) : ChatEffect
    data class ShowErrorRemovingReaction(val throwable: Throwable) : ChatEffect
    data object ScrollChatToStart : ChatEffect
    data object NavigateBack : ChatEffect
    data object ShowReactionSelector : ChatEffect
    data class ShowMessageOptions(val messageId: Int, val messageContent: String) : ChatEffect
}