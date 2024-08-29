package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockStreamTopics(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.get(urlPattern)

    fun withStreamTopics() {
        wireMockServer.stubFor(matcher.willReturn(WireMock.ok(AssetsUtils.fromAssets("topics/Topics.json"))))
    }

    companion object {
        val urlPattern = WireMock.urlMatching("/api/v1/users/me/.+/topics")
        fun WireMockServer.streamTopics(block: MockStreamTopics.() -> Unit) {
            MockStreamTopics(this).apply(block)
        }
    }
}