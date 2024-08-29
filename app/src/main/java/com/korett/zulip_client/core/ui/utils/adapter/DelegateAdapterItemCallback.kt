package com.korett.zulip_client.core.ui.utils.adapter

import androidx.recyclerview.widget.DiffUtil

open class DelegateAdapterItemCallback : DiffUtil.ItemCallback<DelegateItem>() {
    override fun areItemsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean {
        return oldItem::class == newItem::class && oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: DelegateItem, newItem: DelegateItem): Boolean {
        return oldItem.compareToOther(newItem)
    }

    override fun getChangePayload(oldItem: DelegateItem, newItem: DelegateItem): Any? {
        return oldItem.payload(newItem)
    }
}