package com.korett.zulip_client.core.data.remote_source.mapper

import com.korett.zulip_client.core.data.remote_source.model.StreamModelNetwork
import com.korett.zulip_client.core.domain.model.StreamModel

fun StreamModelNetwork.toStreamDomain(isSubscribed: Boolean) = StreamModel(
    id = streamId,
    name = name,
    isSubscribed = isSubscribed
)