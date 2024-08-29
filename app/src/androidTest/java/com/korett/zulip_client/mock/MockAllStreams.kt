package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockAllStreams(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.get(urlPattern)

    fun withAllStreams() {
        wireMockServer.stubFor(matcher.willReturn(WireMock.ok(AssetsUtils.fromAssets("streams/AllStreams.json"))))
    }

    companion object {
        val urlPattern = WireMock.urlPathMatching("/api/v1/streams")
        fun WireMockServer.allStreams(block: MockAllStreams.() -> Unit) {
            MockAllStreams(this).apply(block)
        }
    }
}