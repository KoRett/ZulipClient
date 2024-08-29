package com.korett.zulip_client.presentation.chat.mapper

import com.korett.zulip_client.core.ui.model.DateModelUi
import com.korett.zulip_client.presentation.chat.adapter.date.DateDelegateItem

fun DateModelUi.toDateDelegate(): DateDelegateItem =
    DateDelegateItem(id = this.date, value = this)