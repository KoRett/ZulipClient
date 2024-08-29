package com.korett.zulip_client.core.ui.view

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class ReactionMatcher(private val reaction: String) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("with reaction property: $reaction")
    }

    override fun matchesSafely(view: View): Boolean {
        if (view is EmojiView) {
            return view.emoji == reaction
        }
        return false
    }
}

class ReactionCountMatcher(private val count: Int) : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("with count property: $count")
    }

    override fun matchesSafely(view: View): Boolean {
        if (view is EmojiView) {
            return view.count == count
        }
        return false
    }
}
