package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.topic

import com.korett.zulip_client.core.ui.model.TopicModelUi
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

class TopicDelegateItem(
    private val id: String,
    private val value: TopicModelUi
) : DelegateItem {
    override fun id() = id
    override fun content() = value
    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as TopicDelegateItem).value == value
    }
}