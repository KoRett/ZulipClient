package com.korett.zulip_client.presentation.chat

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.view.KEmojiView
import com.korett.zulip_client.core.ui.view.KFlexBoxLayout
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher

object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {
    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val progressBar = KProgressBar { withId(R.id.progressBar) }
    val recyclerChat = KRecyclerView(
        builder = { withId(R.id.recyclerChat) },
        itemTypeBuilder = {
            itemType(ChatFragmentScreen::KOwnMessageItem)
            itemType(ChatFragmentScreen::KAnotherMessageItem)
            itemType(ChatFragmentScreen::KDateItem)
            itemType(ChatFragmentScreen::KLoadingItem)
            itemType(ChatFragmentScreen::KErrorItem)
        })

    val toolbarChat = KToolbar { withId(R.id.toolbarChat) }
    val textTopic = KTextView { withId(R.id.textTopic) }
    val buttonSent = KButton { withId(R.id.buttonSent) }
    val editTextMessage = KEditText { withId(R.id.editTextMessage) }

    class KOwnMessageItem(parent: Matcher<View>) : KRecyclerItem<KOwnMessageItem>(parent) {
        val textContent = KTextView { withId(R.id.textOwnContent) }
        val ownReactions = KFlexBoxLayout { withId(R.id.ownReactions) }
    }

    class KAnotherMessageItem(parent: Matcher<View>) : KRecyclerItem<KAnotherMessageItem>(parent) {
        val imageAvatar = KImageView { withId(R.id.imageMessageAvatar) }
        val textContent = KTextView { withId(R.id.textAnotherContent) }

        val reactions = KFlexBoxLayout(parent) {
            withId(R.id.anotherReactions)
        }

        val firstReaction = KEmojiView {
            isDescendantOfA {
                withId(R.id.anotherReactions)
                isDescendantOfA { withMatcher(parent) }
            }
            onPosition(0)
        }
    }

    class KDateItem(parent: Matcher<View>) : KRecyclerItem<KDateItem>(parent) {
        val date = KTextView { withId(R.id.textDate) }
    }

    class KLoadingItem(parent: Matcher<View>) : KRecyclerItem<KLoadingItem>(parent) {
        val progressBar = KProgressBar { withId(R.id.itemChatProgressBar) }
    }

    class KErrorItem(parent: Matcher<View>) : KRecyclerItem<KErrorItem>(parent) {
        val errorText = KTextView { withId(R.id.textError) }
        val retryButton = KButton { withId(R.id.buttonRefresh) }
    }
}