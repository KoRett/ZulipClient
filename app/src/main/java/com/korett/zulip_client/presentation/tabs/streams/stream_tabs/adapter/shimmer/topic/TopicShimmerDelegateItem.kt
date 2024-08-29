package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.shimmer.topic

import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

class TopicShimmerDelegateItem(
    private val id: Int
) : DelegateItem {
    override fun content() = id

    override fun id() = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as TopicShimmerDelegateItem).content() == content()
    }
}

val TOPIC_SHIMMER_ITEMS = listOf(
    TopicShimmerDelegateItem(1),
    TopicShimmerDelegateItem(2),
    TopicShimmerDelegateItem(3)
)