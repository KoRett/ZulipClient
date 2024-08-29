package com.korett.zulip_client.core.ui.utils.adapter

interface DelegateItem {
    fun id(): Any
    fun content(): Any
    fun compareToOther(other: DelegateItem): Boolean
    fun payload(other: Any): Payloadable = Payloadable.None
    interface Payloadable {
        object None : Payloadable
    }
}