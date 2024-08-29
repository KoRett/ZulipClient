package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockMessageSentScenario(private val wireMockServer: WireMockServer) {

    private val eventMatcher
        get() = WireMock.get(urlEventPattern)
            .inScenario(EVENT_SEQUENCE_SCENARIO)

    private val messageByIdMatcher
        get() = WireMock.get(urlMessageByIdPattern)
            .inScenario(EVENT_SEQUENCE_SCENARIO)

    private val sentMessageMatcher
        get() = WireMock.post(urlSentMessagePattern)
            .inScenario(EVENT_SEQUENCE_SCENARIO)

    fun withSuccess() {
        wireMockServer.stubFor(
            eventMatcher
                .whenScenarioStateIs(STARTED_STATE)
                .willReturn(WireMock.ok(AssetsUtils.fromAssets("events/HeartbeatEvent.json")))
        )

        wireMockServer.stubFor(
            eventMatcher
                .whenScenarioStateIs(HEARTBEAT_STATE)
                .willReturn(
                    WireMock.ok(AssetsUtils.fromAssets("events/HeartbeatEvent.json"))
                )
        )

        wireMockServer.stubFor(
            eventMatcher
                .whenScenarioStateIs(NEW_OWN_MESSAGE_STATE)
                .willReturn(
                    WireMock.ok(AssetsUtils.fromAssets("events/NewOwnMessageEvent.json"))
                )
        )

        wireMockServer.stubFor(
            messageByIdMatcher
                .whenScenarioStateIs(NEW_OWN_MESSAGE_STATE)
                .willReturn(
                    WireMock.ok(AssetsUtils.fromAssets("messages/NewOwnMessageById.json"))
                )
                .willSetStateTo(HEARTBEAT_STATE)
        )

        wireMockServer.stubFor(
            sentMessageMatcher
                .whenScenarioStateIs(STARTED_STATE)
                .willReturn(WireMock.ok())
                .willSetStateTo(NEW_OWN_MESSAGE_STATE)
        )
    }

    companion object {
        const val STARTED_STATE = "Started"
        const val HEARTBEAT_STATE = "heartbeat_state"
        const val EVENT_SEQUENCE_SCENARIO = "Event sequence scenario"
        const val NEW_OWN_MESSAGE_STATE = "new_own_message_state"

        val urlEventPattern =
            WireMock.urlMatching("/api/v1/events\\?event_queue_longpoll_timeout_seconds=.+")
        val urlMessageByIdPattern =
            WireMock.urlMatching("/api/v1/messages/\\d*")
        val urlSentMessagePattern =
            WireMock.urlMatching("/api/v1/messages\\?to=.+&topic=.+&content=.+&type=.+")


        fun WireMockServer.messageSentScenario(block: MockMessageSentScenario.() -> Unit) {
            MockMessageSentScenario(this).apply(block)
        }
    }
}