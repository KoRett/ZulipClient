package com.korett.zulip_client.presentation.another_profile.state

import com.korett.zulip_client.core.ui.model.UserProfileUi
import com.korett.zulip_client.core.ui.utils.LceState

data class AnotherProfileState(
    val profile: LceState<UserProfileUi>
)