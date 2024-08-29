package com.korett.zulip_client.core.domain.model

data class ReactionModel(
    val reactionCode: String,
    val count: Int,
    val isSelected: Boolean
)