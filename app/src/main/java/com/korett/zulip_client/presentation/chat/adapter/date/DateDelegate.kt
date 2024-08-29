package com.korett.zulip_client.presentation.chat.adapter.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.core.ui.model.DateModelUi
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemChatDateBinding

class DateDelegate : AdapterDelegate() {
    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemChatDateBinding.inflate(
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
        (holder as ViewHolder).bind(item.content() as DateModelUi)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is DateDelegateItem

    class ViewHolder(private val binding: ItemChatDateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DateModelUi) {
            binding.textDate.text = model.date
        }
    }
}