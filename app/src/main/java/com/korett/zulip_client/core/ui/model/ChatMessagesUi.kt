package com.korett.zulip_client.core.ui.model

data class ChatMessagesUi(
    val ownMessages: List<OwnMessageModelUi>,
    val anotherMessages: List<AnotherMessageModelUi>
)