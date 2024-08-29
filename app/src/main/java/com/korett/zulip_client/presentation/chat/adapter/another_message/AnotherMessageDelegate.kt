package com.korett.zulip_client.presentation.chat.adapter.another_message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korett.zulip_client.R
import com.korett.zulip_client.core.common.extension.fromHtmlToSpannedString
import com.korett.zulip_client.core.ui.model.ReactionModelUi
import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemReceivedMessageBinding
import com.korett.zulip_client.presentation.chat.adapter.addButton
import com.korett.zulip_client.presentation.chat.adapter.addEmoji

class AnotherMessageDelegate(
    private val messageLongClickListener: (view: View, messageId: Int, messageContent: String) -> Unit,
    private val addReactionClickListener: (messageId: Int) -> Unit,
    private val reactionClickListener: (messageId: Int, reactionModel: ReactionModelUi) -> Unit
) : AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemReceivedMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            messageLongClickListener,
            addReactionClickListener
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(
            item.content() as AnotherMessageModelUi,
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
        val model = item.content() as AnotherMessageModelUi
        if (payload is AnotherMessageDelegateItem.ChangePayload.ReactionsChanged) {
            (holder as ViewHolder).bindReactions(payload.reactions, model.id, reactionClickListener)
        } else {
            (holder as ViewHolder).bind(model, reactionClickListener)
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is AnotherMessageDelegateItem

    class ViewHolder(
        private val binding: ItemReceivedMessageBinding,
        private val messageLongClickListener: (view: View, messageId: Int, messageContent: String) -> Unit,
        private val addReactionClickListener: (messageId: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: AnotherMessageModelUi,
            reactionClickListener: (messageId: Int, reactionModel: ReactionModelUi) -> Unit
        ) {
            with(binding.receivedMessage) {
                textUsername.text = model.senderFullName

                textContent.text = model.content.fromHtmlToSpannedString()

                Glide.with(imageAvatar.context)
                    .load(model.avatarUrl ?: R.drawable.person_ic)
                    .placeholder(R.drawable.loading_placeholder)
                    .into(imageAvatar)

                this.setOnLongClickListener {
                    messageLongClickListener(textContent, model.id, textContent.text.toString())
                    true
                }

                bindReactions(model.reactions, model.id, reactionClickListener)
            }
        }

        fun bindReactions(
            reactions: List<ReactionModelUi>,
            messageId: Int,
            reactionClickListener: (messageId: Int, reactionModel: ReactionModelUi) -> Unit
        ) {
            binding.receivedMessage.reactions.removeAllViews()

            reactions.forEach { reactionModel ->
                binding.receivedMessage.reactions.addEmoji(reactionModel) {
                    reactionClickListener(messageId, reactionModel)
                }
            }

            binding.receivedMessage.reactions.isVisible = reactions.isNotEmpty()
            if (reactions.isNotEmpty()) {
                binding.receivedMessage.reactions.addButton {
                    addReactionClickListener(messageId)
                }
            }
        }
    }
}