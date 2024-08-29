package com.korett.zulip_client.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.korett.zulip_client.core.data.database.entity.MessageEntity
import com.korett.zulip_client.core.data.database.entity.relationship.MessageWithReactions

@Dao
interface MessageDao {

    @Transaction
    @Query(
        "SELECT message.* FROM message INNER JOIN stream ON message.stream_id LIKE stream.id " +
                "WHERE topic_name LIKE :topicName AND name LIKE :streamName"
    )
    suspend fun getTopicMessagesWithReactions(
        topicName: String,
        streamName: String
    ): List<MessageWithReactions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMessage(messageEntities: MessageEntity): Long

    @Query(
        "DELETE FROM message WHERE topic_name LIKE :topicName " +
                "AND :streamName LIKE (SELECT name FROM stream WHERE stream_id LIKE id)"
    )
    suspend fun deleteTopicMessages(topicName: String, streamName: String)

    @Query("DELETE FROM message WHERE id LIKE (SELECT MAX(id) FROM message)")
    suspend fun deleteLastAddedMessage()

}