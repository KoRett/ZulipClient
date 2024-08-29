package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.shimmer.stream

import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

class StreamShimmerDelegateItem(
    private val id: Int
) : DelegateItem {
    override fun content() = id

    override fun id() = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as StreamShimmerDelegateItem).content() == content()
    }
}


val STREAM_SHIMMER_ITEMS = listOf(
    StreamShimmerDelegateItem(1),
    StreamShimmerDelegateItem(2),
    StreamShimmerDelegateItem(3),
    StreamShimmerDelegateItem(4),
    StreamShimmerDelegateItem(5),
    StreamShimmerDelegateItem(7),
    StreamShimmerDelegateItem(8),
    StreamShimmerDelegateItem(9)
)
