package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.mapper

import com.korett.zulip_client.core.ui.model.StreamModelUi
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.stream.StreamDelegateItem

fun StreamModelUi.toStreamDelegate(): StreamDelegateItem =
    StreamDelegateItem(
        id = id,
        value = this
    )
