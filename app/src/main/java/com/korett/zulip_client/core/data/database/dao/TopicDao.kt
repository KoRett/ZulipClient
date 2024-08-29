package com.korett.zulip_client.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.korett.zulip_client.core.data.database.entity.TopicEntity

@Dao
interface TopicDao {

    @Query("SELECT topic.* FROM topic INNER JOIN stream ON topic.stream_id == stream.id WHERE stream.name LIKE :streamName")
    suspend fun getStreamTopics(streamName: String): List<TopicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopics(topicEntities: List<TopicEntity>)

}