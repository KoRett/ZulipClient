package com.korett.zulip_client.presentation.chat

import android.content.Intent
import androidx.fragment.app.testing.FragmentScenario
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.korett.zulip_client.R
import com.korett.zulip_client.core.data.remote_source.mapper.toColor
import com.korett.zulip_client.mock.MockAddReactionScenario.Companion.addReactionScenario
import com.korett.zulip_client.mock.MockAllStreams.Companion.allStreams
import com.korett.zulip_client.mock.MockDeleteEventQueue.Companion.deleteEventQueue
import com.korett.zulip_client.mock.MockMessageEvent.Companion.events
import com.korett.zulip_client.mock.MockMessageSentScenario.Companion.messageSentScenario
import com.korett.zulip_client.mock.MockNewestMessages.Companion.newestMessages
import com.korett.zulip_client.mock.MockOwnUserData.Companion.ownUserData
import com.korett.zulip_client.mock.MockReactionList.Companion.reactionList
import com.korett.zulip_client.mock.MockRegisterMessageEvents.Companion.registerMessageEvents
import com.korett.zulip_client.mock.MockStreamTopics.Companion.streamTopics
import com.korett.zulip_client.mock.MockSubscribedStreams.Companion.subscribedStreams
import com.korett.zulip_client.mock.MockUnreadMessages.Companion.unreadMessages
import com.korett.zulip_client.presentation.MainActivity
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.StreamTabScreen
import com.korett.zulip_client.util.AppTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ChatFragmentTest : TestCase() {

    @get:Rule
    val rule = AppTestRule()

    @Test
    fun testUiElements() = before {
        rule.wiremockRule.newestMessages { withMessages() }
        rule.wiremockRule.registerMessageEvents { withRegister() }
        rule.wiremockRule.ownUserData { withUserData() }
        rule.wiremockRule.events { withHeartbeatEvents() }
    }.after {
        rule.wiremockRule.resetAll()
    }.run {
        step("Start chat fragment") {
            val streamName = "general"
            val topicName = "swimming turtles"
            val colorInt = topicName.toColor()
            val colorStr = "#${Integer.toHexString(colorInt).substring(2)}"

            FragmentScenario.launchInContainer(
                ChatFragment::class.java,
                ChatFragment.createArgs(streamName, topicName, colorInt),
                R.style.Theme_ZulipClient
            )
            ChatFragmentScreen {
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                step("Check toolbar title and color") {
                    toolbarChat.isVisible()
                    toolbarChat.hasTitle(context.getString(R.string.hash_stream_name, streamName))
                    toolbarChat.hasBackgroundColor(colorStr)
                }
                step("Check topic text") {
                    textTopic.isVisible()
                    textTopic.hasText(context.getString(R.string.topic_colon_hash_name, topicName))
                }
                step("Check input field and button") {
                    editTextMessage.isVisible()
                    editTextMessage.hasHint(R.string.write_and_ellipsis)

                    buttonSent.isVisible()
                    step("Check button isn't selected") {
                        buttonSent.isNotSelected()
                    }
                    step("Type in field some text and check button is selected") {
                        editTextMessage.typeText("some text")
                        buttonSent.isSelected()
                    }
                }
                step("Check chat recycler when state is content") {
                    progressBar.isGone()
                    recyclerChat.isVisible()
                    step("Check first reaction") {
                        recyclerChat.firstChild<ChatFragmentScreen.KAnotherMessageItem> {
                            reactions.isVisible()
                            firstReaction.isVisible()
                            firstReaction.hasCount(1)
                            firstReaction.hasReaction("😉")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testSentMessage() = before {
        rule.wiremockRule.newestMessages { withMessages() }
        rule.wiremockRule.registerMessageEvents { withRegister() }
        rule.wiremockRule.ownUserData { withUserData() }
        rule.wiremockRule.messageSentScenario { withSuccess() }
    }.after {
        rule.wiremockRule.resetAll()
    }.run {
        step("Start chat fragment") {
            val streamName = "general"
            val topicName = "swimming turtles"
            val colorInt = topicName.toColor()

            FragmentScenario.launchInContainer(
                ChatFragment::class.java,
                ChatFragment.createArgs(streamName, topicName, colorInt),
                R.style.Theme_ZulipClient
            )
            ChatFragmentScreen {
                step("Check input field and button") {
                    editTextMessage.isVisible()
                    editTextMessage.hasHint(R.string.write_and_ellipsis)

                    buttonSent.isVisible()
                    step("Check button isn't selected") {
                        buttonSent.isNotSelected()
                    }
                }
                step("Check chat recycler when state is content") {
                    progressBar.isGone()
                    recyclerChat.isVisible()
                    step("Type in field some_text and check button is selected") {
                        editTextMessage.typeText("some text")
                        buttonSent {
                            isSelected()
                            click()
                        }
                    }
                    step("Check sent message") {
                        recyclerChat.firstChild<ChatFragmentScreen.KOwnMessageItem> {
                            isVisible()
                            textContent.isVisible()
                            textContent.hasText("some text")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testAddReaction() = before {
        rule.wiremockRule.newestMessages { withMessages() }
        rule.wiremockRule.registerMessageEvents { withRegister() }
        rule.wiremockRule.ownUserData { withUserData() }
        rule.wiremockRule.addReactionScenario { withSuccess() }
        rule.wiremockRule.reactionList { withReactionList() }
    }.after {
        rule.wiremockRule.resetAll()
    }.run {
        step("Start chat fragment") {
            val streamName = "general"
            val topicName = "swimming turtles"
            val colorInt = topicName.toColor()

            FragmentScenario.launchInContainer(
                ChatFragment::class.java,
                ChatFragment.createArgs(streamName, topicName, colorInt),
                R.style.Theme_ZulipClient
            )
            ChatFragmentScreen {
                step("Check chat recycler when state is content") {
                    progressBar.isGone()
                    recyclerChat.isVisible()
                    step("Check first reaction and click on it") {
                        recyclerChat.firstChild<ChatFragmentScreen.KAnotherMessageItem> {
                            isVisible()
                            reactions.isVisible()
                            firstReaction.hasReaction("😉")
                            firstReaction.hasCount(1)
                            firstReaction.click()
                        }
                    }
                    step("Check addition of first reaction") {
                        recyclerChat.firstChild<ChatFragmentScreen.KAnotherMessageItem> {
                            isVisible()
                            reactions.isVisible()
                            firstReaction.hasReaction("😉")
                            firstReaction.hasCount(2)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testOpenChat() = before {
        rule.wiremockRule.subscribedStreams { withSubscribedStreams() }
        rule.wiremockRule.allStreams { withAllStreams() }
        rule.wiremockRule.streamTopics { withStreamTopics() }
        rule.wiremockRule.unreadMessages { withMessages() }
        rule.wiremockRule.newestMessages { withMessages() }
        rule.wiremockRule.ownUserData { withUserData() }
        rule.wiremockRule.registerMessageEvents { withRegister() }
        rule.wiremockRule.events { withHeartbeatEvents() }
        rule.wiremockRule.deleteEventQueue { success() }
    }.after {
        rule.wiremockRule.resetAll()
    }.run {
        step("Start main activity") {
            ActivityScenario.launch<MainActivity>(getMainActivityIntent())
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            StreamTabScreen {
                val streamName = "general"
                val topicName = "swimming turtles"
                val colorInt = topicName.toColor()
                val colorStr = "#${Integer.toHexString(colorInt).substring(2)}"

                step("Open first stream by click on it") {
                    recyclerView.firstChild<StreamTabScreen.KStreamItem> {
                        textName.hasText(context.getString(R.string.hash_stream_name, streamName))
                        click()
                    }

                    step("Open chat by click on topic") {
                        recyclerView.childAt<StreamTabScreen.KTopicItem>(1) {
                            textName.hasText(topicName)
                            textMessageCount.hasText(context.getString(R.string.count_mes, 99))
                            click()
                        }
                    }

                    ChatFragmentScreen {
                        step("Check toolbar title and color") {
                            toolbarChat.isVisible()
                            toolbarChat.hasTitle(
                                context.getString(
                                    R.string.hash_stream_name,
                                    streamName
                                )
                            )
                            toolbarChat.hasBackgroundColor(colorStr)
                        }
                        step("Check topic text") {
                            textTopic.isVisible()
                            textTopic.hasText(
                                context.getString(
                                    R.string.topic_colon_hash_name,
                                    topicName
                                )
                            )
                        }
                        step("Check input field and button") {
                            editTextMessage.isVisible()
                            editTextMessage.hasHint(R.string.write_and_ellipsis)

                            buttonSent.isVisible()
                            step("Check button isn't selected") {
                                buttonSent.isNotSelected()
                            }
                            step("Type in field some text and check button is selected") {
                                editTextMessage.typeText("some text")
                                buttonSent.isSelected()
                            }
                        }
                        step("Check chat recycler when state is content") {
                            progressBar.isGone()
                            recyclerChat.isVisible()
                            step("Check first reaction") {
                                recyclerChat.firstChild<ChatFragmentScreen.KAnotherMessageItem> {
                                    reactions.isVisible()
                                    firstReaction.isVisible()
                                    firstReaction.hasCount(1)
                                    firstReaction.hasReaction("😉")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getMainActivityIntent(): Intent {
        return Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
    }
}