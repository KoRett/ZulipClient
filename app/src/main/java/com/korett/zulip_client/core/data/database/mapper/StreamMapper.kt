package com.korett.zulip_client.core.data.database.mapper

import com.korett.zulip_client.core.data.database.entity.StreamEntity
import com.korett.zulip_client.core.domain.model.StreamModel

fun StreamEntity.toStreamDomain(): StreamModel =
    StreamModel(
        id = id,
        name = name,
        isSubscribed = isSubscribed
    )

fun StreamModel.toStreamEntity(): StreamEntity =
    StreamEntity(
        id = id,
        name = name,
        isSubscribed = isSubscribed
    )