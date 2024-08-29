package com.korett.zulip_client.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "reaction",
    primaryKeys = ["reaction_code", "message_id"],
    foreignKeys = [
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["message_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("reaction_code"), Index("message_id")]
)
data class ReactionEntity(
    @ColumnInfo(name = "reaction_code")
    val reactionCode: String,
    @ColumnInfo(name = "message_id")
    val messageId: Int,
    val count: Int,
    @ColumnInfo(name = "is_selected")
    val isSelected: Boolean
)