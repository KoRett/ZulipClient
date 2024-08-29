package com.korett.zulip_client.core.data.repository

import com.korett.zulip_client.core.data.database.dao.UserDao
import com.korett.zulip_client.core.data.database.mapper.toUserEntity
import com.korett.zulip_client.core.data.database.mapper.toUserModel
import com.korett.zulip_client.core.data.local_storage.LocalStorage
import com.korett.zulip_client.core.data.remote_source.ZulipRemoteSource
import com.korett.zulip_client.core.data.remote_source.mapper.toUserDomain
import com.korett.zulip_client.core.domain.model.UserModel
import com.korett.zulip_client.core.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val zulipRemoteSource: ZulipRemoteSource,
    private val localStorage: LocalStorage,
    private val userDAO: UserDao
) : UserRepository {

    override suspend fun getUsersOwnId() =
        localStorage.getUsersOwnId() ?: zulipRemoteSource.getOwnData().userId.also {
            localStorage.saveUsersOwnId(it)
        }

    override fun getAllUsers(): Flow<List<UserModel>> = flow {
        var anotherUsers = userDAO.getAllUsers().map { it.toUserModel() }

        if (anotherUsers.isNotEmpty()) {
            emit(anotherUsers)
        }

        anotherUsers = getAnotherActiveUsers(getUsersOwnId()).map { it.toUserDomain() }
        emit(anotherUsers)

        userDAO.saveUsers(anotherUsers.map { it.toUserEntity() })

        anotherUsers = getAnotherActiveUsers(getUsersOwnId()).map { anotherUser ->
            coroutineScope {
                async { anotherUser.toUserDomain(getUserStatus(anotherUser.userId)) }
            }
        }.awaitAll()

        emit(anotherUsers)
    }

    override suspend fun getUserDataById(userId: Int): UserModel = coroutineScope {
        zulipRemoteSource
            .getUserDataById(userId)
            .user.toUserDomain(getUserStatus(userId))
    }

    override suspend fun getOwnUserData(): UserModel {
        val userResponse = zulipRemoteSource.getOwnData()
        return userResponse.toUserDomain(getUserStatus(userResponse.userId))
    }

    private suspend fun getAnotherActiveUsers(usersOwnId: Int) =
        zulipRemoteSource.getAllUsers().members.filter { user ->
            !user.isBot && user.isActive && user.userId != usersOwnId
        }

    private suspend fun getUserStatus(userId: Int) =
        zulipRemoteSource.getUserPresenceById(userId).presence.aggregated.status
}