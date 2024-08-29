package com.korett.zulip_client.core.data.database.entity.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.korett.zulip_client.core.data.database.entity.MessageEntity
import com.korett.zulip_client.core.data.database.entity.ReactionEntity

data class MessageWithReactions (
    @Embedded
    val messageEntity: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "message_id",
        entity = ReactionEntity::class
    )
    val reactionEntities: List<ReactionEntity>
)