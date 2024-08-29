package com.korett.zulip_client.presentation.tabs.people.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.model.UserModelUi
import com.korett.zulip_client.core.ui.model.UserStatusUi
import com.korett.zulip_client.core.ui.utils.adapter.AdapterDelegate
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.databinding.ItemUserBinding

class UserDelegate(
    private val onClickListener: (userModel: UserModelUi) -> Unit
) : AdapterDelegate() {

    override fun onCreateViewHolder(parent: ViewGroup) =
        ViewHolder(
            ItemUserBinding.inflate(
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
        (holder as ViewHolder).bind(item.content() as UserModelUi, onClickListener)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int,
        payloads: List<DelegateItem.Payloadable>
    ) {
        val payload = payloads.firstOrNull()
        val model = item.content() as UserModelUi
        if (payload is UserDelegateItem.ChangePayload.StatusChanged) {
            (holder as ViewHolder).bindStatus(payload.status)
        } else {
            (holder as ViewHolder).bind(model, onClickListener)
        }
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is UserDelegateItem

    class ViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: UserModelUi,
            onClickListener: (userModel: UserModelUi) -> Unit
        ) {
            with(binding) {
                root.setOnClickListener {
                    onClickListener(model)
                }

                textName.text = model.username
                textEmail.text = model.email

                Glide.with(imageAvatar.context)
                    .load(model.avatarUrl)
                    .placeholder(R.drawable.loading_placeholder)
                    .into(imageAvatar)
            }

            bindStatus(model.status)
        }

        fun bindStatus(status: UserStatusUi) {
            binding.imageOnlineStatus.setImageResource(status.color)
        }
    }

}