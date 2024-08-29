package com.korett.zulip_client.presentation.chat.adapter.error

import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

data object ErrorDelegateItem : DelegateItem {
    override fun id(): Int = ID
    override fun content(): Int = ID
    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as ErrorDelegateItem).content() == content()
    }

    private const val ID = 1
}