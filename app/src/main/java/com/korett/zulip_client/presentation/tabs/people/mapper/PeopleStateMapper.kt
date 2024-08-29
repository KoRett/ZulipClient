package com.korett.zulip_client.presentation.tabs.people.mapper

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.people.state.PeopleState
import com.korett.zulip_client.presentation.tabs.people.state.PeopleStateUi
import javax.inject.Inject

interface PeopleStateMapper : (PeopleState) -> PeopleStateUi

class PeopleStateMapperImpl @Inject constructor() : PeopleStateMapper {
    override fun invoke(state: PeopleState): PeopleStateUi {
        return when (val users = state.users) {
            is LceState.Content -> {
                val usersDelegateItems = users.data.map { it.toUserDelegate() }
                PeopleStateUi(LceState.Content(usersDelegateItems))
            }

            is LceState.Error -> PeopleStateUi(users)
            is LceState.Loading -> PeopleStateUi(users)
        }
    }
}