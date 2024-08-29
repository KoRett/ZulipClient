package com.korett.zulip_client.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["name", "stream_id"],
            childColumns = ["topic_name", "stream_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id"), Index("topic_name"), Index("stream_id")]
)
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "sender_id")
    val senderId: Int,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,
    @ColumnInfo(name = "sender_full_name")
    val senderFullName: String,
    val content: String,
    val timestamp: Long,
    @ColumnInfo(name = "topic_name")
    val topicName: String,
    @ColumnInfo(name = "stream_id")
    val streamId: Int
)