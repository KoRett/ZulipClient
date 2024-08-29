package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.korett.zulip_client.util.AssetsUtils.fromAssets

class MockNewestMessages(private val wireMockServer: WireMockServer) {
    private val matcher = WireMock.get(urlPattern)

    fun withMessages() {
        wireMockServer.stubFor(
            matcher.willReturn(
                ok(fromAssets("messages/NewestMessages.json"))
                    .withFixedDelay(1000)
            )
        )
    }

    companion object {
        val urlPattern =
            WireMock.urlMatching("/api/v1/messages\\?num_after=0&anchor=newest&narrow=.+&num_before=\\d+")

        fun WireMockServer.newestMessages(block: MockNewestMessages.() -> Unit) {
            MockNewestMessages(this).apply(block)
        }
    }
}