package com.korett.zulip_client.presentation.chat.adapter.load

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemChatLoadBinding

class LoadDelegate : AdapterDelegate() {
    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemChatLoadBinding.inflate(
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

    override fun isOfViewType(item: DelegateItem): Boolean = item is LoadingDelegateItem

    class ViewHolder(binding: ItemChatLoadBinding) : RecyclerView.ViewHolder(binding.root)

}