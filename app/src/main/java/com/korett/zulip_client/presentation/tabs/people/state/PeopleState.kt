package com.korett.zulip_client.presentation.tabs.people.state

import com.korett.zulip_client.core.ui.model.UserModelUi
import com.korett.zulip_client.core.ui.utils.LceState

data class PeopleState(
    val users: LceState<List<UserModelUi>>,
    val lastQuery: String
)