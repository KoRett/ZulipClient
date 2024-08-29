package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.stream

import com.korett.zulip_client.core.ui.model.StreamModelUi
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

class StreamDelegateItem(
    private val id: Int,
    private val value: StreamModelUi
) : DelegateItem {
    override fun content() = value

    override fun id() = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as StreamDelegateItem).value == value
    }

    override fun payload(other: Any): DelegateItem.Payloadable {
        if (other is StreamDelegateItem) {
            if (value.isOpen != other.value.isOpen) {
                return ChangePayload.OpenStateChanged(other.value.isOpen)
            }
        }
        return DelegateItem.Payloadable.None
    }

    sealed class ChangePayload : DelegateItem.Payloadable {
        data class OpenStateChanged(val isOpen: Boolean) : ChangePayload()
    }
}