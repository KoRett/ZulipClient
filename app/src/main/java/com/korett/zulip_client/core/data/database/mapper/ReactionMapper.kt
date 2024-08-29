package com.korett.zulip_client.core.data.database.mapper

import com.korett.zulip_client.core.data.database.entity.ReactionEntity
import com.korett.zulip_client.core.domain.model.ReactionModel

fun ReactionEntity.toReactionDomain() =
    ReactionModel(
        reactionCode = reactionCode,
        count = count,
        isSelected = isSelected,
    )

fun ReactionModel.toReactionEntity(messageId: Int) =
    ReactionEntity(
        reactionCode = reactionCode,
        messageId = messageId,
        count = count,
        isSelected = isSelected,
    )