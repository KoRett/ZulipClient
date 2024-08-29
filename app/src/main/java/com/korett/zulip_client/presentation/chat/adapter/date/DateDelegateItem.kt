package com.korett.zulip_client.presentation.chat.adapter.date

import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.core.ui.model.DateModelUi

data class DateDelegateItem(
    private val id: String,
    private val value: DateModelUi
) : DelegateItem {
    override fun content() = value

    override fun id(): String = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as DateDelegateItem).content() == content()
    }
}