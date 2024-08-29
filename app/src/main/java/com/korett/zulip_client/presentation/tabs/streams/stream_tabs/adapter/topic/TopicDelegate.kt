package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.topic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.model.TopicModelUi
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemTopicBinding

class TopicDelegate(
    private val onClickListener: (topic: TopicModelUi) -> Unit
) : AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemTopicBinding.inflate(
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
        (holder as ViewHolder).bind(item.content() as TopicModelUi, onClickListener)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is TopicDelegateItem

    class ViewHolder(private val binding: ItemTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: TopicModelUi,
            onClickListener: (topic: TopicModelUi) -> Unit
        ) {
            with(binding) {
                root.setOnClickListener {
                    onClickListener(model)
                }

                root.setBackgroundColor(model.color)

                textNameTopic.text = model.name
                textMessageCount.text = textMessageCount.context.getString(R.string.count_mes, model.messageCount)
            }
        }
    }
}