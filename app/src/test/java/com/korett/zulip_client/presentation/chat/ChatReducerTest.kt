package com.korett.zulip_client.presentation.chat

import com.korett.zulip_client.core.ui.model.ChatMessagesUi
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.chat.state.PreviousMessagesState
import com.korett.zulip_client.presentation.chat.test_data.ChatReducerTestData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class ChatReducerTest : BehaviorSpec({
    ChatReducerTestData().apply {
        Given("ChatReducer") {
            val reducer = ChatReducer()
            When("reduce") {
                And("State is initial") {
                    And("Event is Ui") {
                        And("Loading when screen is opened") {
                            val actual = reducer.reduce(ChatEvent.Ui.Init, initialState)
                            val expectedStatus = LceState.Loading
                            val expectedCommand = ChatCommand.InitChat(
                                initialState.streamName,
                                initialState.topicName
                            )

                            Then("Messages state should be is loading, start loading message") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.commands shouldContain expectedCommand
                            }
                        }
                    }

                    And("Event is Internal") {
                        And("Messages is loading") {
                            val actual =
                                reducer.reduce(ChatEvent.Internal.ChatLoading, initialState)
                            val expectedStatus = LceState.Loading

                            Then("Messages state should be is loading") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                            }
                        }

                        And("Error loading messages") {
                            val actual =
                                reducer.reduce(
                                    ChatEvent.Internal.ChatError(loadError),
                                    initialState
                                )
                            val expectedStatus = LceState.Error(loadError)

                            Then("Messages state should be is error") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                            }
                        }

                        And("Messages is loaded") {
                            And("Messages less then ${initialState.initialMessageCount}") {
                                val actual = reducer.reduce(
                                    ChatEvent.Internal.ChatLoaded(fewChatMessages),
                                    initialState
                                )
                                val expectedStatus = LceState.Content(fewChatMessages)
                                val expectedPreviousMessagesState = PreviousMessagesState.End

                                Then("Messages should be loaded, not need load previous messages") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }

                            And("Message greater than or equal to ${initialState.initialMessageCount}") {
                                val actual = reducer.reduce(
                                    ChatEvent.Internal.ChatLoaded(manyChatMessages),
                                    initialState
                                )
                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedPreviousMessagesState = PreviousMessagesState.Waiting

                                Then("Messages should be loaded, need load previous messages") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }
                        }
                    }
                }

                And("State is error") {
                    And("Event is Ui") {
                        And("Loading when screen is refreshed") {
                            val actual = reducer.reduce(ChatEvent.Ui.Init, errorState)
                            val expectedStatus = LceState.Error(loadError)
                            val expectedCommandInitChat = ChatCommand.InitChat(
                                initialState.streamName,
                                initialState.topicName
                            )

                            Then("Messages state should be is error, start loading message") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.commands shouldContain expectedCommandInitChat
                            }
                        }
                    }

                    And("Event is Internal") {
                        And("Messages is loading") {
                            val actual =
                                reducer.reduce(ChatEvent.Internal.ChatLoading, errorState)
                            val expectedStatus = LceState.Loading

                            Then("Messages state should be is loading") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                            }
                        }

                        And("Error loading messages") {
                            val actual =
                                reducer.reduce(
                                    ChatEvent.Internal.ChatError(loadError),
                                    errorState
                                )
                            val expectedStatus = LceState.Error(loadError)

                            Then("Messages state should be is error") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                            }
                        }

                        And("Messages is loaded") {
                            And("Messages less then ${errorState.previousMessageCount}") {
                                val actual = reducer.reduce(
                                    ChatEvent.Internal.ChatLoaded(fewChatMessages),
                                    errorState
                                )
                                val expectedStatus = LceState.Content(fewChatMessages)
                                val expectedPreviousMessagesState = PreviousMessagesState.End

                                Then("Messages should be loaded, not need load previous messages") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }

                            And("Message greater than or equal to ${errorState.previousMessageCount}") {
                                val actual = reducer.reduce(
                                    ChatEvent.Internal.ChatLoaded(manyChatMessages),
                                    initialState
                                )
                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedPreviousMessagesState = PreviousMessagesState.Waiting

                                Then("Messages should be loaded, need load previous messages") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }
                        }
                    }
                }

                And("State is content") {
                    And("Event is Ui") {
                        And("Navigate back") {
                            val actual = reducer.reduce(ChatEvent.Ui.NavigateBack, contentState)

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedEffect = ChatEffect.NavigateBack

                            Then("Messages state is content, should navigate back") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.effects shouldContain expectedEffect
                            }
                        }

                        And("Open message options") {
                            val messageId = 1
                            val messageContent = "some_text"
                            val actual = reducer.reduce(
                                ChatEvent.Ui.ShowMessageOptions(messageId, messageContent),
                                contentState
                            )

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedEffect = ChatEffect.ShowMessageOptions(messageId, messageContent)

                            Then("Messages state is content, should open message options") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.effects shouldContain expectedEffect
                            }
                        }

                        And("Sent message") {
                            val messageContent = "some_message_content"

                            val actual = reducer.reduce(
                                ChatEvent.Ui.SentMessage(messageContent),
                                contentState
                            )

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedCommand = ChatCommand.SentMessage(
                                contentState.streamName,
                                contentState.topicName,
                                messageContent
                            )

                            Then("Messages state is content, message is being sent") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.commands shouldContain expectedCommand
                            }
                        }

                        And("Select message") {
                            val selectedMessageId = 423

                            val actual = reducer.reduce(
                                ChatEvent.Ui.SelectMessageToAddReaction(selectedMessageId),
                                contentState
                            )

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedEffect = ChatEffect.ShowReactionSelector

                            Then("Messages state is content, message was selected and reaction selection is showed") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.state.selectedMessageId shouldBe selectedMessageId
                                actual.effects shouldContain expectedEffect
                            }
                        }

                        And("Select not existed reaction") {
                            val selectedReaction = "😁"

                            val actual = reducer.reduce(
                                ChatEvent.Ui.ReactionSelected(selectedReaction),
                                contentState
                            )

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedCommand = ChatCommand.AddReaction(
                                contentState.selectedMessageId!!,
                                "😁"
                            )

                            Then("Messages state is content, reaction is added") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.state.selectedMessageId shouldBe null
                                actual.commands shouldContain expectedCommand
                            }
                        }

                        And("Reaction is clicked") {
                            And("Reaction was not added by user") {
                                val selectedMessageId = 423
                                val selectedReaction = "😁"

                                val actual = reducer.reduce(
                                    ChatEvent.Ui.ReactionClicked(
                                        selectedMessageId,
                                        selectedReaction,
                                        false
                                    ),
                                    contentState
                                )

                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedCommand = ChatCommand.AddReaction(
                                    selectedMessageId,
                                    "😁"
                                )

                                Then("Messages state is content, reaction is added") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.commands shouldContain expectedCommand
                                }
                            }

                            And("Reaction was added by user") {
                                val selectedMessageId = 423
                                val selectedReaction = "😁"

                                val actual = reducer.reduce(
                                    ChatEvent.Ui.ReactionClicked(
                                        selectedMessageId,
                                        selectedReaction,
                                        true
                                    ),
                                    contentState
                                )

                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedCommand = ChatCommand.RemoveReaction(
                                    selectedMessageId,
                                    "😁"
                                )

                                Then("Messages state is content, reaction is removed") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.commands shouldContain expectedCommand
                                }
                            }
                        }

                        And("Previous messages state is error") {
                            And("Event to retry load previous messages") {
                                val actual = reducer.reduce(
                                    ChatEvent.Ui.RetryLoadPreviousMessages,
                                    contentState.copy(previousMessagesState = PreviousMessagesState.Error)
                                )

                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedCommand = ChatCommand.LoadPreviousMessages(
                                    1,
                                    contentState.streamName,
                                    contentState.topicName,
                                    contentState.previousMessageCount
                                )
                                val expectedPreviousMessagesState = PreviousMessagesState.Loading

                                Then("Messages state is content, previous messages is loading") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                    actual.commands shouldContain expectedCommand
                                }
                            }
                        }

                        And("Event to load previous messages") {
                            And("Previous messages state is error") {
                                val actual = reducer.reduce(
                                    ChatEvent.Ui.LoadPreviousMessages,
                                    contentState.copy(previousMessagesState = PreviousMessagesState.Error)
                                )

                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedCommand = ChatCommand.LoadPreviousMessages(
                                    1,
                                    contentState.streamName,
                                    contentState.topicName,
                                    contentState.previousMessageCount
                                )
                                val expectedPreviousMessagesState = PreviousMessagesState.Error

                                Then("Messages state is content, previous messages is not loading") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.commands shouldNotContain expectedCommand
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }

                            And("Previous messages state is nothing") {
                                val actual = reducer.reduce(
                                    ChatEvent.Ui.LoadPreviousMessages,
                                    contentState.copy(previousMessagesState = PreviousMessagesState.Waiting)
                                )

                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedCommand = ChatCommand.LoadPreviousMessages(
                                    1,
                                    contentState.streamName,
                                    contentState.topicName,
                                    contentState.previousMessageCount
                                )
                                val expectedPreviousMessagesState = PreviousMessagesState.Loading

                                Then("Messages state is content, previous messages is loading") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.commands shouldContain expectedCommand
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }

                            And("Previous message state is end") {
                                val actual = reducer.reduce(
                                    ChatEvent.Ui.LoadPreviousMessages,
                                    contentState.copy(previousMessagesState = PreviousMessagesState.End)
                                )

                                val expectedStatus = LceState.Content(manyChatMessages)
                                val expectedCommand = ChatCommand.LoadPreviousMessages(
                                    1,
                                    contentState.streamName,
                                    contentState.topicName,
                                    contentState.previousMessageCount
                                )
                                val expectedPreviousMessagesState = PreviousMessagesState.End

                                Then("Messages state is content, previous messages is not loading") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.commands shouldNotContain expectedCommand
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }
                        }
                    }

                    And("Event is Internal") {
                        And("Error loading previous messages") {
                            val actual = reducer.reduce(
                                ChatEvent.Internal.PreviousMessagesLoadError(loadError),
                                contentState
                            )

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedPreviousMessagesState = PreviousMessagesState.Error

                            Then("Messages state is content, previous messages state is error") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                            }
                        }

                        And("Error sending message") {
                            val actual = reducer.reduce(
                                ChatEvent.Internal.ErrorSendingMessage(loadError),
                                contentState
                            )

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedEffect = ChatEffect.ShowErrorSendingMessage(loadError)

                            Then("Messages state is content, show error sending message") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.effects shouldContain expectedEffect
                            }
                        }

                        And("Success sending message") {
                            val actual = reducer.reduce(
                                ChatEvent.Internal.SuccessSendingMessage,
                                contentState
                            )
                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedEffect = ChatEffect.ScrollChatToStart

                            Then("Messages state is content, show sending message (scroll to beginning of chat)") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.effects shouldContain expectedEffect
                            }
                        }

                        And("Error adding reaction") {
                            val actual = reducer.reduce(
                                ChatEvent.Internal.ErrorAddingReaction(loadError),
                                contentState
                            )

                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedEffect = ChatEffect.ShowErrorAddingReaction(loadError)

                            Then("Messages state is content, show error adding reaction") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.effects shouldContain expectedEffect
                            }
                        }

                        And("Error removing reaction") {
                            val actual = reducer.reduce(
                                ChatEvent.Internal.ErrorRemovingReaction(loadError),
                                contentState
                            )
                            val expectedStatus = LceState.Content(manyChatMessages)
                            val expectedEffect = ChatEffect.ShowErrorRemovingReaction(loadError)

                            Then("Messages state is content, show error removing reaction") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                                actual.effects shouldContain expectedEffect
                            }
                        }

                        And("Previous messages is loaded") {
                            And("Messages less then ${contentState.previousMessageCount}") {
                                val actual = reducer.reduce(
                                    ChatEvent.Internal.PreviousMessagesLoaded(fewChatMessages),
                                    contentState
                                )

                                val expectedChatMessages = ChatMessagesUi(
                                    ownMessages = fewChatMessages.ownMessages + manyChatMessages.ownMessages,
                                    anotherMessages = fewChatMessages.anotherMessages + manyChatMessages.anotherMessages
                                )
                                val expectedStatus = LceState.Content(expectedChatMessages)
                                val expectedPreviousMessagesState = PreviousMessagesState.End

                                Then("Messages state is content, not need load previous messages") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }

                            And("Message greater than or equal to ${contentState.previousMessageCount}") {
                                val actual = reducer.reduce(
                                    ChatEvent.Internal.PreviousMessagesLoaded(fewPreviousChatItems),
                                    contentState
                                )

                                val expectedChatMessages = ChatMessagesUi(
                                    ownMessages = fewPreviousChatItems.ownMessages + manyChatMessages.ownMessages,
                                    anotherMessages = fewPreviousChatItems.anotherMessages + manyChatMessages.anotherMessages
                                )
                                val expectedStatus = LceState.Content(expectedChatMessages)
                                val expectedPreviousMessagesState = PreviousMessagesState.End

                                Then("Messages state is content, need load previous messages") {
                                    actual.state.chatMessagesState shouldBe expectedStatus
                                    actual.state.previousMessagesState shouldBe expectedPreviousMessagesState
                                }
                            }
                        }

                        And("Changed messages is loaded") {
                            val actual = reducer.reduce(
                                ChatEvent.Internal.ChangedMessages(changedChatMessages),
                                contentState
                            )
                            val expectedStatus = LceState.Content(comparedChatMessages)

                            Then("Messages state is content and messages have been updated") {
                                actual.state.chatMessagesState shouldBe expectedStatus
                            }
                        }
                    }
                }
            }
        }
    }
})