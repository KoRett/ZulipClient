package com.korett.zulip_client.util

import androidx.test.espresso.intent.rule.IntentsRule
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class AppTestRule: TestRule {

    val wiremockRule = WireMockRule()
    private val intentsRule = IntentsRule()

    override fun apply(base: Statement?, description: Description?): Statement {
        return RuleChain.outerRule(intentsRule)
            .around(wiremockRule)
            .apply(base, description)
    }
}