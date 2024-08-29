package com.korett.zulip_client.core.domain.model

data class UserModel(
    val id: Int,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val status: UserStatus = UserStatus.Unknown
)

sealed class UserStatus {
    data object Active : UserStatus()
    data object Idle : UserStatus()
    data object Offline : UserStatus()
    data object Unknown : UserStatus()
}