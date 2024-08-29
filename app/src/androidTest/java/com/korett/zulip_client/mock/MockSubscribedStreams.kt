package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockSubscribedStreams(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.get(urlPattern)

    fun withSubscribedStreams() {
        wireMockServer.stubFor(matcher.willReturn(WireMock.ok(AssetsUtils.fromAssets("streams/SubscribedStreams.json"))))
    }

    companion object {
        val urlPattern = WireMock.urlPathMatching(".+api/v1/users/me/subscriptions")
        fun WireMockServer.subscribedStreams(block: MockSubscribedStreams.() -> Unit) {
            MockSubscribedStreams(this).apply(block)
        }
    }
}