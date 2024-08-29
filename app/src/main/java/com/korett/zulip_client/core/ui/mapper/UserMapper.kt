package com.korett.zulip_client.core.ui.mapper

import com.korett.zulip_client.core.domain.model.UserModel
import com.korett.zulip_client.core.domain.model.UserStatus
import com.korett.zulip_client.core.ui.model.UserModelUi
import com.korett.zulip_client.core.ui.model.UserProfileStatusUi
import com.korett.zulip_client.core.ui.model.UserProfileUi
import com.korett.zulip_client.core.ui.model.UserStatusUi

fun UserModel.toUserModelUi(): UserModelUi = UserModelUi(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    status = status.toUserStatusUi()
)

fun UserStatus.toUserStatusUi(): UserStatusUi = when (this) {
    UserStatus.Active -> UserStatusUi.Active
    UserStatus.Idle -> UserStatusUi.Idle
    UserStatus.Offline -> UserStatusUi.Offline
    UserStatus.Unknown -> UserStatusUi.Unknown
}

fun UserModel.toUserProfileUi(): UserProfileUi = UserProfileUi(
    id = id,
    username = username,
    email = email,
    avatarUrl = avatarUrl,
    status = status.toUserProfileStatusUi()
)

fun UserStatus.toUserProfileStatusUi(): UserProfileStatusUi = when (this) {
    UserStatus.Active -> UserProfileStatusUi.Active
    UserStatus.Idle -> UserProfileStatusUi.Idle
    UserStatus.Offline -> UserProfileStatusUi.Offline
    UserStatus.Unknown -> throw IllegalArgumentException()
}