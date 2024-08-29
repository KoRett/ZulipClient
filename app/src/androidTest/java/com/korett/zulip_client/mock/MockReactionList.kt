package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.korett.zulip_client.util.AssetsUtils

class MockReactionList(private val wireMockServer: WireMockServer) {
    private val matcher = WireMock.get(urlPattern)

    fun withReactionList() {
        wireMockServer.stubFor(
            matcher.willReturn(
                WireMock.ok(
                    AssetsUtils.fromAssets("reaction/ReactionList.json")
                )
            )
        )
    }

    companion object {
        val urlPattern = WireMock.urlMatching("/static/generated/emoji/emoji_codes.json")
        fun WireMockServer.reactionList(block: MockReactionList.() -> Unit) {
            MockReactionList(this).apply(block)
        }
    }
}