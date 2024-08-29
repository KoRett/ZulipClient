package com.korett.zulip_client.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.korett.zulip_client.core.data.database.entity.ReactionEntity

@Dao
interface ReactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReactions(reactionEntities: List<ReactionEntity>): List<Long>

}