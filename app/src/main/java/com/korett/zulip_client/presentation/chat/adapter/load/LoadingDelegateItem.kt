package com.korett.zulip_client.presentation.chat.adapter.load

import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

data object LoadingDelegateItem : DelegateItem {
    override fun id(): Int = ID
    override fun content(): Int = ID
    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as LoadingDelegateItem).content() == content()
    }

    private const val ID = 1
}