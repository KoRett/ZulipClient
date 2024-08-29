package com.korett.zulip_client.core.ui.model

data class ReactionModelUi(
    val reactionCode: String,
    val count: Int,
    val isSelected: Boolean
)