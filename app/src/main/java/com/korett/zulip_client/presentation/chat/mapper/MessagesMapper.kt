package com.korett.zulip_client.presentation.chat.mapper

import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.core.ui.model.ChatMessagesUi
import com.korett.zulip_client.core.ui.model.DateModelUi
import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.presentation.chat.adapter.another_message.AnotherMessageDelegateItem
import com.korett.zulip_client.presentation.chat.adapter.own_message.OwnMessageDelegateItem
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.min

fun mapMessageModelsToDelegateMessagesWithDate(
    chatMessages: ChatMessagesUi,
    locale: Locale
): List<DelegateItem> {
    val numberFormatter = SimpleDateFormat("d", locale)
    val monthFormatter = SimpleDateFormat("MMM", locale)

    fun getDateByTimestamp(timestampInMillis: Long): String {
        val number = numberFormatter.format(timestampInMillis)
        val month = monthFormatter.format(timestampInMillis)
            .removeSuffix(".")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        return "$number $month"
    }

    val delegateItemList: MutableList<DelegateItem> = mutableListOf()

    val ownMessages: MutableList<OwnMessageModelUi> =
        chatMessages.ownMessages.toMutableList()
    val anotherMessages: MutableList<AnotherMessageModelUi> =
        chatMessages.anotherMessages.toMutableList()

    var ownMessageIndex = 0
    var anotherMessageIndex = 0
    var lastDate = ""

    fun addDateIfNeed(timestamp: Long) {
        val currentDate = getDateByTimestamp(timestamp * 1000)
        if (currentDate != lastDate) {
            val dateModelUi = DateModelUi(currentDate)
            delegateItemList.add(dateModelUi.toDateDelegate())
            lastDate = currentDate
        }
    }

    while (ownMessageIndex <= ownMessages.lastIndex && anotherMessageIndex <= anotherMessages.lastIndex) {
        val ownMessage = ownMessages[ownMessageIndex]
        val receivedMessage = anotherMessages[anotherMessageIndex]

        val currentTimestamp = min(ownMessage.timestamp, receivedMessage.timestamp)
        addDateIfNeed(currentTimestamp)

        if (ownMessage.timestamp < receivedMessage.timestamp) {
            delegateItemList.add(ownMessage.toOwnMessageDelegate())
            ownMessageIndex++
        } else {
            delegateItemList.add(receivedMessage.toAnotherMessageDelegate())
            anotherMessageIndex++
        }
    }

    while (ownMessageIndex <= ownMessages.lastIndex) {
        val ownMessage = ownMessages[ownMessageIndex]

        addDateIfNeed(ownMessage.timestamp)

        delegateItemList.add(OwnMessageDelegateItem(ownMessage.id, ownMessage))
        ownMessageIndex++
    }

    while (anotherMessageIndex <= anotherMessages.lastIndex) {
        val receivedMessage = anotherMessages[anotherMessageIndex]

        addDateIfNeed(receivedMessage.timestamp)

        delegateItemList.add(AnotherMessageDelegateItem(receivedMessage.id, receivedMessage))
        anotherMessageIndex++
    }

    return delegateItemList.reversed()
}