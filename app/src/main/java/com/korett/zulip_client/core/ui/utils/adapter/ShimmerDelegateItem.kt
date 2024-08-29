package com.korett.zulip_client.core.ui.utils.adapter

class ShimmerDelegateItem(
    private val id: Int
) : DelegateItem {
    override fun content() = id

    override fun id() = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as ShimmerDelegateItem).content() == content()
    }
}

val BIG_SHIMMER_ITEMS = listOf(
    ShimmerDelegateItem(1),
    ShimmerDelegateItem(2),
    ShimmerDelegateItem(3),
    ShimmerDelegateItem(4),
    ShimmerDelegateItem(5),
    ShimmerDelegateItem(7),
    ShimmerDelegateItem(8),
    ShimmerDelegateItem(9)
)
