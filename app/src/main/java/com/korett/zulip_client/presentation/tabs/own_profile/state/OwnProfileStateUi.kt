package com.korett.zulip_client.presentation.tabs.own_profile.state

import com.korett.zulip_client.core.ui.model.UserProfileUi
import com.korett.zulip_client.core.ui.utils.LceState

data class OwnProfileStateUi(
    val profile: LceState<UserProfileUi>
)