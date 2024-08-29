package com.korett.zulip_client.core.ui.mapper

import com.korett.zulip_client.core.domain.model.StreamModel
import com.korett.zulip_client.core.ui.model.StreamModelUi
import com.korett.zulip_client.core.ui.model.TopicState

fun StreamModel.toStreamUi(): StreamModelUi =
    StreamModelUi(
        id = id,
        name = name,
        isOpen = false,
        topics = TopicState.Loading,
        isSubscribed = isSubscribed
    )

fun StreamModelUi.toStreamDomain(): StreamModel =
    StreamModel(
        id = id,
        name = name,
        isSubscribed = isSubscribed
    )