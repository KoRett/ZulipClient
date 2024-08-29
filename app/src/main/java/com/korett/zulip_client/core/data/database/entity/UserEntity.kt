package com.korett.zulip_client.core.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?
)