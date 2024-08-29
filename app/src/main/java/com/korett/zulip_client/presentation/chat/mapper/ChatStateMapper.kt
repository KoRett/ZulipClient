package com.korett.zulip_client.presentation.chat.mapper

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.presentation.chat.adapter.error.ErrorDelegateItem
import com.korett.zulip_client.presentation.chat.adapter.load.LoadingDelegateItem
import com.korett.zulip_client.presentation.chat.state.ChatState
import com.korett.zulip_client.presentation.chat.state.ChatStateUi
import com.korett.zulip_client.presentation.chat.state.PreviousMessagesState
import java.util.Locale
import javax.inject.Inject

interface ChatStateMapper : (ChatState) -> ChatStateUi

class ChatStateMapperImpl @Inject constructor(private val locale: Locale) : ChatStateMapper {
    override fun invoke(state: ChatState): ChatStateUi {
        when (state.chatMessagesState) {
            is LceState.Content -> {
                var chatItems =
                    mapMessageModelsToDelegateMessagesWithDate(state.chatMessagesState.data, locale)

                when (state.previousMessagesState) {
                    PreviousMessagesState.Error -> {
                        chatItems = addErrorItem(chatItems)
                    }

                    PreviousMessagesState.Loading -> {
                        chatItems = addLoadItem(chatItems)
                    }

                    PreviousMessagesState.Waiting -> Unit

                    PreviousMessagesState.End -> Unit
                }

                return ChatStateUi(LceState.Content(chatItems))
            }

            is LceState.Error -> return ChatStateUi(LceState.Error(state.chatMessagesState.error))

            LceState.Loading -> return ChatStateUi(LceState.Loading)
        }
    }

    private fun addLoadItem(chatItems: List<DelegateItem>): List<DelegateItem> {
        val mutableChatItems = chatItems.toMutableList()
        mutableChatItems.add(LoadingDelegateItem)
        return mutableChatItems.toList()
    }

    private fun addErrorItem(chatItems: List<DelegateItem>): List<DelegateItem> {
        val mutableChatItems = chatItems.toMutableList()
        mutableChatItems.add(ErrorDelegateItem)
        return mutableChatItems.toList()
    }

}