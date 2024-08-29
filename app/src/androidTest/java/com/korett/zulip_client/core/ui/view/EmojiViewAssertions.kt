package com.korett.zulip_client.core.ui.view

import androidx.test.espresso.assertion.ViewAssertions
import io.github.kakaocup.kakao.common.assertions.BaseAssertions

interface EmojiViewAssertions : BaseAssertions {
    fun hasReaction(reaction: String) {
        view.check(
            ViewAssertions.matches(
                ReactionMatcher(reaction)
            )
        )
    }

    fun hasCount(count: Int) {
        view.check(
            ViewAssertions.matches(
                ReactionCountMatcher(count)
            )
        )
    }
}
