package com.korett.zulip_client.presentation.tabs.people.adapter.user

import com.korett.zulip_client.core.ui.model.UserModelUi
import com.korett.zulip_client.core.ui.model.UserStatusUi
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

class UserDelegateItem(
    private val id: Int,
    private val value: UserModelUi
) : DelegateItem {
    override fun content() = value

    override fun id() = id

    override fun compareToOther(other: DelegateItem): Boolean {
        return (other as UserDelegateItem).content() == content()
    }

    override fun payload(other: Any): DelegateItem.Payloadable {
        if (other is UserDelegateItem) {
            if (value.status != other.value.status) {
                return ChangePayload.StatusChanged(other.value.status)
            }
        }
        return DelegateItem.Payloadable.None
    }

    sealed class ChangePayload : DelegateItem.Payloadable {
        data class StatusChanged(val status: UserStatusUi) : ChangePayload()
    }
}