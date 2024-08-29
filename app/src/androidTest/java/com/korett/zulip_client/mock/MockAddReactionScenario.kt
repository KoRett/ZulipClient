package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockAddReactionScenario(private val wireMockServer: WireMockServer) {

    private val eventMatcher
        get() = WireMock.get(urlEventPattern)
            .inScenario(EVENT_SEQUENCE_SCENARIO)

    private val messageByIdMatcher
        get() = WireMock.get(urlMessageByIdPattern)
            .inScenario(EVENT_SEQUENCE_SCENARIO)

    private val addReactionMatcher
        get() = WireMock.post(urlAddReactionPattern)
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
                .whenScenarioStateIs(CHANGED_MESSAGE_STATE)
                .willReturn(
                    WireMock.ok(AssetsUtils.fromAssets("events/ChangedMessageEvent.json"))
                )
        )

        wireMockServer.stubFor(
            messageByIdMatcher
                .whenScenarioStateIs(CHANGED_MESSAGE_STATE)
                .willReturn(
                    WireMock.ok(AssetsUtils.fromAssets("messages/ChangedMessageById.json"))
                )
                .willSetStateTo(HEARTBEAT_STATE)
        )

        wireMockServer.stubFor(
            addReactionMatcher
                .whenScenarioStateIs(STARTED_STATE)
                .willReturn(WireMock.ok())
                .willSetStateTo(CHANGED_MESSAGE_STATE)
        )
    }

    companion object {
        const val STARTED_STATE = "Started"
        const val HEARTBEAT_STATE = "heartbeat_state"
        const val EVENT_SEQUENCE_SCENARIO = "Event sequence scenario"
        const val CHANGED_MESSAGE_STATE = "changed_message_state"

        val urlEventPattern =
            WireMock.urlMatching("/api/v1/events\\?event_queue_longpoll_timeout_seconds=.+")
        val urlMessageByIdPattern =
            WireMock.urlMatching("/api/v1/messages/\\d*")
        val urlAddReactionPattern =
            WireMock.urlMatching("/api/v1/messages/\\d+/reactions.+")


        fun WireMockServer.addReactionScenario(block: MockAddReactionScenario.() -> Unit) {
            MockAddReactionScenario(this).apply(block)
        }
    }
}