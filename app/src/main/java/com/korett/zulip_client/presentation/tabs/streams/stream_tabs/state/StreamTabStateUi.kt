package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem

data class StreamTabStateUi(
    val streamTabDelegateItems: LceState<List<DelegateItem>>
)