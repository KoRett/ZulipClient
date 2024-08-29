package com.korett.zulip_client.presentation.chat.adapter.own_message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.core.common.extension.fromHtmlToSpannedString
import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.model.ReactionModelUi
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemOwnMessageBinding
import com.korett.zulip_client.presentation.chat.adapter.addButton
import com.korett.zulip_client.presentation.chat.adapter.addEmoji

class OwnMessageDelegate(
    private val longMessageClickListener: (view: View, messageId: Int, messageContent: String) -> Unit,
    private val addReactionClickListener: (messageId: Int) -> Unit,
    private val reactionClickListener: (messageId: Int, reactionModel: ReactionModelUi) -> Unit
) : AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemOwnMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            longMessageClickListener,
            addReactionClickListener
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(
            item.content() as OwnMessageModelUi,
            reactionClickListener
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
        payloads: List<DelegateItem.Payloadable>
    ) {
        val payload = payloads.firstOrNull()
        val model = item.content() as OwnMessageModelUi
        if (payload is OwnMessageDelegateItem.ChangePayload.ReactionsChanged) {
            (holder as ViewHolder).bindReactions(payload.reactions, model.id, reactionClickListener)
        } else {
            (holder as ViewHolder).bind(model, reactionClickListener)
        }
    }


    override fun isOfViewType(item: DelegateItem): Boolean = item is OwnMessageDelegateItem

    class ViewHolder(
        private val binding: ItemOwnMessageBinding,
        private val longMessageClickListener: (view: View, messageId: Int, messageContent: String) -> Unit,
        private val addReactionClickListener: (messageId: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: OwnMessageModelUi,
            reactionClickListener: (messageId: Int, reactionModel: ReactionModelUi) -> Unit
        ) {
            binding.textOwnContent.text = model.content.fromHtmlToSpannedString()

            binding.root.setOnLongClickListener {
                longMessageClickListener(
                    binding.textOwnContent,
                    model.id,
                    binding.textOwnContent.text.toString()
                )
                true
            }

            bindReactions(model.reactions, model.id, reactionClickListener)
        }

        fun bindReactions(
            reactions: List<ReactionModelUi>,
            messageId: Int,
            reactionClickListener: (messageId: Int, reactionModel: ReactionModelUi) -> Unit
        ) {
            binding.ownReactions.removeAllViews()

            reactions.forEach { reactionModel ->
                binding.ownReactions.addEmoji(reactionModel) {
                    reactionClickListener(messageId, reactionModel)
                }
            }

            binding.ownReactions.isVisible = reactions.isNotEmpty()
            if (reactions.isNotEmpty()) {
                binding.ownReactions.addButton {
                    addReactionClickListener(messageId)
                }
            }
        }
    }
}