package com.korett.zulip_client.presentation.chat.mapper

import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.chat.adapter.error.ErrorDelegateItem
import com.korett.zulip_client.presentation.chat.adapter.load.LoadingDelegateItem
import com.korett.zulip_client.presentation.chat.mapper.test_data.ChatStateMapperTestData
import com.korett.zulip_client.presentation.chat.state.PreviousMessagesState
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Locale

@RunWith(JUnit4::class)
class ChatStateMapperTest : BehaviorSpec({
    ChatStateMapperTestData().apply {
        Given("ChatStateMapper") {
            val chatStateMapper = ChatStateMapperImpl(Locale.getDefault())
            When("Invoke") {
                And("Messages is loading") {
                    val actual = chatStateMapper.invoke(initialState)
                    val expectedChatItems = LceState.Loading

                    Then("Messages state is loading") {
                        actual.chatItemsState shouldBe expectedChatItems
                    }
                }

                And("Error loading messages") {
                    val actual = chatStateMapper.invoke(errorState)
                    val expectedChatItems = LceState.Error(loadError)

                    Then("Error messages state") {
                        actual.chatItemsState shouldBe expectedChatItems
                    }
                }

                And("Messages have been loaded") {

                    And("Previous messages is loading") {
                        val actual = chatStateMapper.invoke(
                            contentState.copy(previousMessagesState = PreviousMessagesState.Loading)
                        )

                        val expectedChatItems = LceState.Content(chatItems + LoadingDelegateItem)

                        Then("Messages with load status at the end") {
                            actual.chatItemsState shouldBe expectedChatItems
                        }
                    }

                    And("Error loading previous messages") {
                        val actual = chatStateMapper.invoke(
                            contentState.copy(previousMessagesState = PreviousMessagesState.Error)
                        )

                        val expectedChatItems = LceState.Content(chatItems + ErrorDelegateItem)

                        Then("Messages with error status at the end") {
                            actual.chatItemsState shouldBe expectedChatItems
                        }
                    }

                    And("Previous messages are waiting to be loaded") {
                        val actual = chatStateMapper.invoke(
                            contentState.copy(previousMessagesState = PreviousMessagesState.Waiting)
                        )

                        val expectedChatItems = LceState.Content(chatItems)

                        Then("Only messages") {
                            actual.chatItemsState shouldBe expectedChatItems
                        }
                    }

                    And("Previous messages are waiting to be loaded") {
                        val actual = chatStateMapper.invoke(
                            contentState.copy(previousMessagesState = PreviousMessagesState.End)
                        )

                        val expectedChatItems = LceState.Content(chatItems)

                        Then("Only messages") {
                            actual.chatItemsState shouldBe expectedChatItems
                        }
                    }
                }
            }
        }
    }
})