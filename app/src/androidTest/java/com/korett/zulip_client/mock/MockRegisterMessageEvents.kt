package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockRegisterMessageEvents(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.post(urlPattern)

    fun withRegister() {
        wireMockServer.stubFor(matcher.willReturn(WireMock.ok(AssetsUtils.fromAssets("events/RegisterMessageChanges.json"))))
    }

    companion object {
        val urlPattern =
            WireMock.urlMatching("/api/v1/register\\?event_types=.+")

        fun WireMockServer.registerMessageEvents(block: MockRegisterMessageEvents.() -> Unit) {
            MockRegisterMessageEvents(this).apply(block)
        }
    }
}