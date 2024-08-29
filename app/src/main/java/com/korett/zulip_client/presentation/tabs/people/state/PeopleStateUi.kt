package com.korett.zulip_client.presentation.tabs.people.state

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.people.adapter.user.UserDelegateItem

data class PeopleStateUi(
    val users: LceState<List<UserDelegateItem>>
)