package com.korett.zulip_client.core.data.database.mapper

import com.korett.zulip_client.core.data.database.entity.UserEntity
import com.korett.zulip_client.core.domain.model.UserModel

fun UserModel.toUserEntity(): UserEntity =
    UserEntity(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl
    )

fun UserEntity.toUserModel(): UserModel =
    UserModel(
        id = id,
        username = username,
        email = email,
        avatarUrl = avatarUrl
    )