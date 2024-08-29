package com.korett.zulip_client.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.korett.zulip_client.core.data.database.dao.MessageDao
import com.korett.zulip_client.core.data.database.dao.ReactionDao
import com.korett.zulip_client.core.data.database.dao.StreamDao
import com.korett.zulip_client.core.data.database.dao.TopicDao
import com.korett.zulip_client.core.data.database.dao.UserDao
import com.korett.zulip_client.core.data.database.entity.MessageEntity
import com.korett.zulip_client.core.data.database.entity.ReactionEntity
import com.korett.zulip_client.core.data.database.entity.StreamEntity
import com.korett.zulip_client.core.data.database.entity.TopicEntity
import com.korett.zulip_client.core.data.database.entity.UserEntity

@Database(
    entities = [
        StreamEntity::class,
        TopicEntity::class,
        MessageEntity::class,
        ReactionEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDao
    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao
    abstract fun topicDao(): TopicDao
    abstract fun reactionDao(): ReactionDao
}