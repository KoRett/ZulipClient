package com.korett.zulip_client.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "topic",
    primaryKeys = ["name", "stream_id"],
    foreignKeys = [
        ForeignKey(
            entity = StreamEntity::class,
            parentColumns = ["id"],
            childColumns = ["stream_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("name"), Index("stream_id")]
)
data class TopicEntity(
    val name: String,
    @ColumnInfo(name = "stream_id")
    val streamId: Int,
    @ColumnInfo(name = "message_count")
    val messageCount: Int,
    val color: Int
)