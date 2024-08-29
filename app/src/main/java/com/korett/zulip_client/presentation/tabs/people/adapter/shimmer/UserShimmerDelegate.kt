package com.korett.zulip_client.presentation.tabs.people.adapter.shimmer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.core.ui.utils.adapter.ShimmerDelegateItem
import com.korett.zulip_client.databinding.ItemUserShimmerBinding

class UserShimmerDelegate : AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemUserShimmerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is ShimmerDelegateItem

    class ViewHolder(binding: ItemUserShimmerBinding) : RecyclerView.ViewHolder(binding.root)

}