package com.korett.zulip_client.presentation.chat.state

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

data class ChatStateUi(
    val chatItemsState: LceState<List<DelegateItem>>
)
