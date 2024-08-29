package com.korett.zulip_client.presentation.tabs.people.mapper

import com.korett.zulip_client.core.ui.model.UserModelUi
import com.korett.zulip_client.presentation.tabs.people.adapter.user.UserDelegateItem

fun UserModelUi.toUserDelegate(): UserDelegateItem =
    UserDelegateItem(id, this)