package com.korett.zulip_client.presentation.another_profile.mapper

import com.korett.zulip_client.presentation.another_profile.state.AnotherProfileState
import com.korett.zulip_client.presentation.another_profile.state.AnotherProfileStateUi
import javax.inject.Inject

interface AnotherProfileStateMapper : (AnotherProfileState) -> AnotherProfileStateUi


class AnotherProfileStateMapperImpl @Inject constructor() : AnotherProfileStateMapper {
    override fun invoke(state: AnotherProfileState): AnotherProfileStateUi {
        return AnotherProfileStateUi(state.profile)
    }
}