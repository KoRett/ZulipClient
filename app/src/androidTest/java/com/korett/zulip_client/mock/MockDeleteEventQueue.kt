package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockDeleteEventQueue(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.delete(urlPattern)

    fun success() {
        wireMockServer.stubFor(matcher.willReturn(WireMock.ok(AssetsUtils.fromAssets("messages/TopicUnreadMessages.json"))))
    }

    companion object {
        val urlPattern = WireMock.urlMatching("/api/v1/events.+")
        fun WireMockServer.deleteEventQueue(block: MockDeleteEventQueue.() -> Unit) {
            MockDeleteEventQueue(this).apply(block)
        }
    }
}