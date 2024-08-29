package com.korett.zulip_client.presentation.chat.mapper

import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.presentation.chat.adapter.own_message.OwnMessageDelegateItem
import com.korett.zulip_client.presentation.chat.adapter.another_message.AnotherMessageDelegateItem

fun OwnMessageModelUi.toOwnMessageDelegate(): OwnMessageDelegateItem =
    OwnMessageDelegateItem(
        id = id,
        value = this
    )

fun AnotherMessageModelUi.toAnotherMessageDelegate(): AnotherMessageDelegateItem =
    AnotherMessageDelegateItem(
        id = id,
        value = this
    )