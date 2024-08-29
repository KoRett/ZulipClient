package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.stream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.model.StreamModelUi
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemStreamBinding

class StreamDelegate(
    private val onClickListener: (streamId: Int, streamName: String) -> Unit,
    private val onOptionClickListener: (view: View, streamModel: StreamModelUi) -> Unit
) : AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemStreamBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClickListener,
            onOptionClickListener
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as StreamModelUi)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
        payloads: List<DelegateItem.Payloadable>
    ) {
        val payload = payloads.firstOrNull()
        val model = item.content() as StreamModelUi
        if (payload is StreamDelegateItem.ChangePayload.OpenStateChanged) {
            (holder as ViewHolder).bindIsOpenState(payload.isOpen)
        } else {
            (holder as ViewHolder).bind(model)
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is StreamDelegateItem

    class ViewHolder(
        private val binding: ItemStreamBinding,
        private val onClickListener: (streamId: Int, streamName: String) -> Unit,
        private val onOptionClickListener: (view: View, streamModel: StreamModelUi) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: StreamModelUi) {
            with(binding) {
                textNameStream.text =
                    textNameStream.context.getString(R.string.hash_stream_name, model.name)

                root.setOnClickListener { onClickListener(model.id, model.name) }

                binding.buttonOptions.setOnClickListener { onOptionClickListener(it, model) }

                bindIsOpenState(model.isOpen)
            }
        }

        fun bindIsOpenState(isOpen: Boolean) {
            with(binding) {
                imageArrow.isSelected = isOpen
                root.isSelected = isOpen
            }
        }
    }

}