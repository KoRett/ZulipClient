package com.korett.zulip_client.core.domain.repository

import com.korett.zulip_client.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsers(): Flow<List<UserModel>>

    suspend fun getUserDataById(userId: Int): UserModel

    suspend fun getOwnUserData(): UserModel

    suspend fun getUsersOwnId(): Int
}