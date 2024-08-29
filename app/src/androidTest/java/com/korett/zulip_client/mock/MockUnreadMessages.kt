package com.korett.zulip_client.mock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.korett.zulip_client.util.AssetsUtils.fromAssets

class MockUnreadMessages(private val wireMockServer: WireMockServer) {


    private val matcher = WireMock.get(urlPattern)

    fun withMessages() {
        wireMockServer.stubFor(matcher.willReturn(ok(fromAssets("messages/TopicUnreadMessages.json"))))
    }

    companion object {
        val urlPattern = WireMock.urlMatching("/api/v1/messages\\?num_before=0&anchor=first_unread&num_after=301&narrow=.+")
        fun WireMockServer.unreadMessages(block: MockUnreadMessages.() -> Unit) {
            MockUnreadMessages(this).apply(block)
        }
    }
}