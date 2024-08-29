package com.korett.zulip_client.presentation.chat.test_data

import com.korett.zulip_client.core.ui.model.AnotherMessageModelUi
import com.korett.zulip_client.core.ui.model.ChatMessagesUi
import com.korett.zulip_client.core.ui.model.OwnMessageModelUi
import com.korett.zulip_client.core.ui.model.ReactionModelUi
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.chat.state.ChatState
import com.korett.zulip_client.presentation.chat.state.PreviousMessagesState
import okio.IOException

class ChatReducerTestData {

    private val streamName = "stream_name"
    private val topicName = "topic_name"
    private val previousMessagesCount = 6
    private val initialMessageCount = 6

    private val usersOwnId = 1
    private val usersAnotherId = 2

    private val senderFullName = "some_name"
    private val messageContent = "some_text"

    val loadError = IOException()

    private val selectedMessageId = 11

    val manyChatMessages =
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
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = true)
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
                        ReactionModelUi(reactionCode = "1f601", count = 3, isSelected = false)
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

    val fewChatMessages =
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
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = true)
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
                        ReactionModelUi(reactionCode = "1f601", count = 3, isSelected = false)
                    )
                )
            )
        )

    val changedChatMessages =
        ChatMessagesUi(
            ownMessages = listOf(
                OwnMessageModelUi(
                    id = 3,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780829,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(
                            reactionCode = "1f601",
                            count = 1,
                            isSelected = false
                        )
                    )
                )
            ),
            anotherMessages = listOf(
                AnotherMessageModelUi(
                    id = 6,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780832,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf(
                        ReactionModelUi(
                            reactionCode = "1f601",
                            count = 1,
                            isSelected = false
                        )
                    )
                )
            )
        )

    val comparedChatMessages =
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
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = true)
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
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = false)
                    )
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
                        ReactionModelUi(reactionCode = "1f601", count = 3, isSelected = false)
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
                    reactions = listOf(
                        ReactionModelUi(reactionCode = "1f601", count = 1, isSelected = false)
                    )
                )
            )
        )


    val manyPreviousChatItems =
        ChatMessagesUi(
            ownMessages = listOf(
                OwnMessageModelUi(
                    id = 7,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780821,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                OwnMessageModelUi(
                    id = 8,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780822,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                OwnMessageModelUi(
                    id = 9,
                    senderId = usersOwnId,
                    content = messageContent,
                    timestamp = 1715780823,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            ),
            anotherMessages = listOf(
                AnotherMessageModelUi(
                    id = 10,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780824,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                AnotherMessageModelUi(
                    id = 11,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780825,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                ),
                AnotherMessageModelUi(
                    id = 12,
                    senderId = usersAnotherId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780826,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            )
        )

    val fewPreviousChatItems =
        ChatMessagesUi(
            ownMessages = emptyList(),
            anotherMessages = listOf(
                AnotherMessageModelUi(
                    id = 12,
                    senderId = usersOwnId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780819,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = listOf()
                ),
                AnotherMessageModelUi(
                    id = 13,
                    senderId = usersOwnId,
                    avatarUrl = null,
                    senderFullName = senderFullName,
                    content = messageContent,
                    timestamp = 1715780820,
                    streamName = streamName,
                    topicName = topicName,
                    reactions = emptyList()
                )
            )
        )


    val initialState
        get() = ChatState(
            chatMessagesState = LceState.Loading,
            streamName,
            topicName,
            selectedMessageId = null,
            previousMessagesState = PreviousMessagesState.Waiting,
            previousMessagesCount,
            initialMessageCount
        )

    val errorState
        get() = ChatState(
            chatMessagesState = LceState.Error(loadError),
            streamName,
            topicName,
            selectedMessageId = null,
            previousMessagesState = PreviousMessagesState.Waiting,
            previousMessagesCount,
            initialMessageCount
        )

    val contentState
        get() = ChatState(
            chatMessagesState = LceState.Content(manyChatMessages),
            streamName,
            topicName,
            selectedMessageId = selectedMessageId,
            previousMessagesState = PreviousMessagesState.Waiting,
            previousMessagesCount,
            initialMessageCount
        )
}