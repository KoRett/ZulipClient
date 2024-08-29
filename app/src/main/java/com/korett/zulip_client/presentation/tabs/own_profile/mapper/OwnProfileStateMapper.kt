package com.korett.zulip_client.presentation.tabs.own_profile.mapper

import com.korett.zulip_client.presentation.tabs.own_profile.state.OwnProfileState
import com.korett.zulip_client.presentation.tabs.own_profile.state.OwnProfileStateUi
import javax.inject.Inject

interface OwnProfileStateMapper : (OwnProfileState) -> OwnProfileStateUi

class OwnProfileStateMapperImpl @Inject constructor() : OwnProfileStateMapper {
    override fun invoke(state: OwnProfileState): OwnProfileStateUi {
        return OwnProfileStateUi(state.profile)
    }
}