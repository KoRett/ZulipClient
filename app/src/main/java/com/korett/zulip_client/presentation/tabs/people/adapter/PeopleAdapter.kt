package com.korett.zulip_client.presentation.tabs.people.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateAdapterItemCallback
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

class PeopleAdapter :
    ListAdapter<DelegateItem, RecyclerView.ViewHolder>(DelegateAdapterItemCallback()) {

    private val delegates: MutableList<AdapterDelegate> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegates[viewType].onCreateViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegates[getItemViewType(position)].onBindViewHolder(holder, getItem(position), position)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        val delegate = delegates[getItemViewType(position)]
        val delegatePayloads = payloads.map { it as DelegateItem.Payloadable }

        val payload = delegatePayloads.firstOrNull()
        if (payload != DelegateItem.Payloadable.None) {
            delegate.onBindViewHolder(holder, getItem(position), position, delegatePayloads)
        } else {
            delegate.onBindViewHolder(holder, getItem(position), position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return delegates.indexOfFirst { it.isOfViewType(getItem(position)) }
    }

    fun addDelegate(delegate: AdapterDelegate) {
        delegates.add(delegate)
    }

}