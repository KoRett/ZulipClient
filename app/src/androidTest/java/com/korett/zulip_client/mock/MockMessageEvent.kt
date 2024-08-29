package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockMessageEvent(private val wireMockServer: WireMockServer) {
    private val matcher
        get() = WireMock.get(urlPattern)

    fun withHeartbeatEvents() {
        wireMockServer.stubFor(
            matcher
                .willReturn(
                    WireMock.ok(AssetsUtils.fromAssets("events/HeartbeatEvent.json"))
                )
        )
    }

    companion object {
        val urlPattern =
            WireMock.urlMatching("/api/v1/events\\?event_queue_longpoll_timeout_seconds=.+")

        fun WireMockServer.events(block: MockMessageEvent.() -> Unit) {
            MockMessageEvent(this).apply(block)
        }
    }
}