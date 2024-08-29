package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockOwnUserData(private val wireMockServer: WireMockServer) {

    private val matcher = WireMock.get(urlPattern)

    fun withUserData() {
        wireMockServer.stubFor(matcher.willReturn(WireMock.ok(AssetsUtils.fromAssets("users/OwnUserData.json"))))
    }

    companion object {
        val urlPattern = WireMock.urlPathMatching("/api/v1/users/me")
        fun WireMockServer.ownUserData(block: MockOwnUserData.() -> Unit) {
            MockOwnUserData(this).apply(block)
        }
    }
}