package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.mapper

import com.korett.zulip_client.core.ui.model.TopicModelUi
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.topic.TopicDelegateItem

fun TopicModelUi.toTopicDelegate(): TopicDelegateItem =
    TopicDelegateItem(
        id = name,
        value = this
    )