package com.korett.zulip_client.core.data.remote_source.mapper

import com.korett.zulip_client.core.data.remote_source.model.AnotherUserModelNetwork
import com.korett.zulip_client.core.data.remote_source.model.OwnUserDataResponse
import com.korett.zulip_client.core.data.remote_source.model.user_presence.UserStatusNetwork
import com.korett.zulip_client.core.domain.model.UserModel
import com.korett.zulip_client.core.domain.model.UserStatus

fun AnotherUserModelNetwork.toUserDomain(): UserModel = UserModel(
    id = userId,
    email = deliveryEmail ?: email,
    username = fullName,
    avatarUrl = avatarUrl,
    status = UserStatus.Unknown
)

fun AnotherUserModelNetwork.toUserDomain(status: UserStatusNetwork): UserModel = UserModel(
    id = userId,
    email = deliveryEmail ?: email,
    username = fullName,
    avatarUrl = avatarUrl,
    status = status.toUserStatusDomain()
)

fun OwnUserDataResponse.toUserDomain(): UserModel = UserModel(
    id = userId,
    email = deliveryEmail ?: email,
    username = fullName,
    avatarUrl = avatarUrl,
    status = UserStatus.Unknown
)

fun OwnUserDataResponse.toUserDomain(status: UserStatusNetwork): UserModel = UserModel(
    id = userId,
    email = deliveryEmail ?: email,
    username = fullName,
    avatarUrl = avatarUrl,
    status = status.toUserStatusDomain()
)

fun UserStatusNetwork.toUserStatusDomain(): UserStatus = when (this) {
    UserStatusNetwork.ACTIVE -> UserStatus.Active
    UserStatusNetwork.IDLE -> UserStatus.Idle
    UserStatusNetwork.OFFLINE -> UserStatus.Offline
}
