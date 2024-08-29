package com.korett.zulip_client.presentation.chat.adapter.own_message

import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.model.ReactionModelUi
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

data class OwnMessageDelegateItem(
    private val id: Int,
    private val value: OwnMessageModelUi
) : DelegateItem {
    override fun content() = value

    override fun id() = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as OwnMessageDelegateItem).value == value
    }

    override fun payload(other: Any): DelegateItem.Payloadable {
        if (other is OwnMessageDelegateItem) {
            if (value.reactions != other.value.reactions) {
                return ChangePayload.ReactionsChanged(other.value.reactions)
            }
        }
        return DelegateItem.Payloadable.None
    }

    sealed class ChangePayload : DelegateItem.Payloadable {
        data class ReactionsChanged(val reactions: List<ReactionModelUi>) : ChangePayload()
    }
}