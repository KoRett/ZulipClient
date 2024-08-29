package com.korett.zulip_client.presentation.chat.mapper.test_data

import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.core.ui.model.ChatMessagesUi
import com.korett.zulip_client.core.ui.model.DateModelUi
import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.model.ReactionModelUi
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.presentation.chat.adapter.another_message.AnotherMessageDelegateItem
import com.korett.zulip_client.presentation.chat.adapter.date.DateDelegateItem
import com.korett.zulip_client.presentation.chat.adapter.own_message.OwnMessageDelegateItem
import com.korett.zulip_client.presentation.chat.state.ChatState
import com.korett.zulip_client.presentation.chat.state.PreviousMessagesState
import okio.IOException

class ChatStateMapperTestData {
    private val streamName = "stream_name"
    private val topicName = "topic_name"
    private val loadMessageCount = 20
    private val initialMessageCount = 20

    private val usersOwnId = 1
    private val usersAnotherId = 2

    val loadError = IOException()

    private val selectedMessageId = 11

    private val senderFullName = "some_name"
    val messageContent = "some_text"

    private val date = "15 Май"

    private val chatMessages =
        ChatMessagesUi(
            ownMessages = listOf(
                OwnMessageModelUi(
                    id = 1,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780827,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "😁", count = 1, isSelected = true)
                    )
                ),
                OwnMessageModelUi(
                    id = 2,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780828,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                OwnMessageModelUi(
                    id = 3,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780829,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf()
                )
            ),
            anotherMessages = listOf(
                AnotherMessageModelUi(
                    id = 4,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780830,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf()
                ),
                AnotherMessageModelUi(
                    id = 5,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780831,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "😁", count = 3, isSelected = false)
                    )
                ),
                AnotherMessageModelUi(
                    id = 6,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780832,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            )
        )

    val chatItems
        get() = listOf<DelegateItem>(
            DateDelegateItem(
                date,
                DateModelUi(date)
            ),
            OwnMessageDelegateItem(
                1,
                OwnMessageModelUi(
                    id = 1,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780827,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "😁", count = 1, isSelected = true)
                    )
                )
            ),
            OwnMessageDelegateItem(
                2,
                OwnMessageModelUi(
                    id = 2,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780828,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            ),
            OwnMessageDelegateItem(
                3,
                OwnMessageModelUi(
                    id = 3,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780829,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            ),
            AnotherMessageDelegateItem(
                4,
                AnotherMessageModelUi(
                    id = 4,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780830,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf()
                )
            ),
            AnotherMessageDelegateItem(
                5,
                AnotherMessageModelUi(
                    id = 5,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780831,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "😁", count = 3, isSelected = false)
                    )
                )
            ),
            AnotherMessageDelegateItem(
                6,
                AnotherMessageModelUi(
                    id = 6,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780832,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            )
        ).reversed()

    val initialState
        get() = ChatState(
            chatMessagesState = LceState.Loading,
            streamName,
            topicName,
            selectedMessageId = null,
            previousMessagesState = PreviousMessagesState.Waiting,
            loadMessageCount,
            initialMessageCount
        )

    val errorState
        get() = ChatState(
            chatMessagesState = LceState.Error(loadError),
            streamName,
            topicName,
            selectedMessageId = null,
            previousMessagesState = PreviousMessagesState.Waiting,
            loadMessageCount,
            initialMessageCount
        )

    val contentState
        get() = ChatState(
            chatMessagesState = LceState.Content(chatMessages),
            streamName,
            topicName,
            selectedMessageId = selectedMessageId,
            previousMessagesState = PreviousMessagesState.Waiting,
            loadMessageCount,
            initialMessageCount
        )
}