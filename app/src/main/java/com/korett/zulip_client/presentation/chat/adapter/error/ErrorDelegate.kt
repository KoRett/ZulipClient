package com.korett.zulip_client.presentation.chat.adapter.error

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemChatErrorBinding

class ErrorDelegate(private val onRetryButtonClick: () -> Unit) : AdapterDelegate() {
    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemChatErrorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onRetryButtonClick
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind()
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is ErrorDelegateItem

    class ViewHolder(
        private val binding: ItemChatErrorBinding,
        private val onRetryButtonClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.buttonRefresh.setOnClickListener {
                onRetryButtonClick()
            }
        }
    }

}