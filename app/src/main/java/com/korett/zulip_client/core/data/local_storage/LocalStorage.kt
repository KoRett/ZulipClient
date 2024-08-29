package com.korett.zulip_client.core.data.local_storage

interface LocalStorage {
    suspend fun getUsersOwnId(): Int?
    suspend fun saveUsersOwnId(usersOwnId: Int)
    suspend fun saveCodepointToNameMap(codepointToNameMap: Map<String, String>)
    suspend fun getCodepointToNameMap(): Map<String, String>?
}