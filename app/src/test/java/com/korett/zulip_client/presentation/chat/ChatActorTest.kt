package com.korett.zulip_client.presentation.chat

import com.korett.zulip_client.presentation.chat.test_data.ChatActorTestData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChatActorTest : BehaviorSpec({
    ChatActorTestData().apply {
        Given("ChatActor") {
            val successActor = ChatActor(
                successChatRepository,
                successGetSortedChatMessagesUseCase,
                successGetSortedPreviousChatMessagesUseCase,
                successGetSortedChangedMessages
            )
            val errorActor = ChatActor(
                errorChatRepository,
                errorGetSortedChatMessagesUseCase,
                errorGetSortedPreviousChatMessagesUseCase,
                errorGetSortedChangedMessages
            )
            When("execute") {
                And("Chat is loading") {
                    And("Success event result") {
                        val actual = successActor.execute(
                            ChatCommand.InitChat(streamName, topicName)
                        ).take(2).toList()

                        val firstExpectedEvent =
                            ChatEvent.Internal.ChatLoaded(chatMessages)
                        val nextExpectedEvent =
                            ChatEvent.Internal.ChangedMessages(changedChatMessages)

                        Then("Chat is loaded and updated") {
                            actual[0] shouldBe firstExpectedEvent
                            actual[1] shouldBe nextExpectedEvent
                        }
                    }

                    And("Error event result") {
                        val actual = errorActor.execute(
                            ChatCommand.InitChat(streamName, topicName)
                        ).take(1).toList()

                        val expectedEvent = ChatEvent.Internal.ChatError(loadError)

                        Then("Chat is not loaded ") {
                            actual[0] shouldBe expectedEvent
                        }
                    }
                }
            }

            And("Sent message") {
                And("Success event result") {
                    val actual = successActor.execute(
                        ChatCommand.SentMessage(streamName, topicName, messageContent)
                    )

                    val expectedEvent = ChatEvent.Internal.SuccessSendingMessage

                    Then("Message has not been sent") {
                        actual.first() shouldBe expectedEvent
                    }
                }

                And("Error event result") {
                    val actual = errorActor.execute(
                        ChatCommand.SentMessage(streamName, topicName, messageContent)
                    )

                    val expectedEvent = ChatEvent.Internal.ErrorSendingMessage(loadError)

                    Then("Event is ${expectedEvent::class.simpleName}") {
                        actual.first() shouldBe expectedEvent
                    }
                }
            }

            And("Add reaction") {
                And("Error event result") {
                    val actual = errorActor.execute(
                        ChatCommand.AddReaction(messageId, reaction)
                    )

                    val expectedEvent = ChatEvent.Internal.ErrorAddingReaction(loadError)

                    Then("Reaction has not been added") {
                        actual.first() shouldBe expectedEvent
                    }
                }
            }

            And("Remove reaction") {
                And("Error event result") {
                    val actual = errorActor.execute(
                        ChatCommand.RemoveReaction(messageId, reaction)
                    )

                    val expectedEvent = ChatEvent.Internal.ErrorRemovingReaction(loadError)

                    Then("Reaction has not been removed") {
                        actual.first() shouldBe expectedEvent
                    }
                }
            }

            And("Load previous messages") {
                And("Success event result") {
                    val actual = successActor.execute(
                        ChatCommand.LoadPreviousMessages(
                            messageId,
                            streamName,
                            topicName,
                            messageLoadCount
                        )
                    )

                    val expectedEvent = ChatEvent.Internal.PreviousMessagesLoaded(
                        expectedPreviousChatItems
                    )

                    Then("Previous messages have been loaded") {
                        actual.first() shouldBe expectedEvent
                    }
                }

                And("Error event result") {
                    val actual = errorActor.execute(
                        ChatCommand.LoadPreviousMessages(
                            messageId,
                            streamName,
                            topicName,
                            messageLoadCount
                        )
                    )

                    val expectedEvent = ChatEvent.Internal.PreviousMessagesLoadError(loadError)

                    Then("Previous messages have not been loaded") {
                        actual.first() shouldBe expectedEvent
                    }
                }
            }
        }
    }
})