package com.korett.zulip_client.core.data.local_storage

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorageImpl @Inject constructor() :
    com.korett.zulip_client.core.data.local_storage.LocalStorage {

    @Volatile
    private var userOwnId: Int? = null

    @Volatile
    private var codepointToNameMap: Map<String, String>? = null

    override suspend fun getUsersOwnId(): Int? = userOwnId

    override suspend fun saveUsersOwnId(usersOwnId: Int) {
        this.userOwnId = usersOwnId
    }

    override suspend fun getCodepointToNameMap(): Map<String, String>? = codepointToNameMap

    override suspend fun saveCodepointToNameMap(codepointToNameMap: Map<String, String>) {
        this.codepointToNameMap = codepointToNameMap
    }
}