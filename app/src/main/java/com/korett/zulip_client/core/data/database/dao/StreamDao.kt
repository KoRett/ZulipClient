package com.korett.zulip_client.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.korett.zulip_client.core.data.database.entity.StreamEntity

@Dao
interface StreamDao {

    @Query("SELECT * FROM stream")
    suspend fun getAllStreams(): List<StreamEntity>

    @Query("SELECT * FROM stream WHERE is_subscribed LIKE 1")
    suspend fun getSubscribedStreams(): List<StreamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStreams(streamsEntities: List<StreamEntity>)

    @Query("SELECT id FROM stream WHERE name LIKE :streamName")
    suspend fun getStreamIdByName(streamName: String): Int?

}