package com.korett.zulip_client.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream")
data class StreamEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    @ColumnInfo(name = "is_subscribed")
    val isSubscribed: Boolean
)