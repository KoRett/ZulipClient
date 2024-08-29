package com.korett.zulip_client.core.ui.utils.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AdapterDelegate {
    abstract fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    )

    abstract fun isOfViewType(item: DelegateItem): Boolean

    open fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
        payloads: List<DelegateItem.Payloadable>
    ) {
        onBindViewHolder(holder, item, position)
    }
}