package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.shimmer.stream

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemStreamShimmerBinding

class StreamShimmerDelegate : AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemStreamShimmerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) = Unit

    override fun isOfViewType(item: DelegateItem): Boolean = item is StreamShimmerDelegateItem

    class ViewHolder(binding: ItemStreamShimmerBinding) : RecyclerView.ViewHolder(binding.root)

}