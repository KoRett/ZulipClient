package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.korett.zulip_client.R
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object StreamTabScreen : KScreen<StreamTabScreen>() {
    override val layoutId: Int = R.layout.fragment_stream_tab
    override val viewClass: Class<*> = StreamTabFragment::class.java

    val recyclerView = KRecyclerView(
        builder = { withId(R.id.recyclerStreams) },
        itemTypeBuilder = {
            itemType(::KStreamItem)
            itemType(::KTopicItem)
        }
    )

    class KStreamItem(parent: Matcher<View>) : KRecyclerItem<KStreamItem>(parent) {
        val textName = KTextView { withId(R.id.textNameStream) }
    }

    class KTopicItem(parent: Matcher<View>) : KRecyclerItem<KTopicItem>(parent) {
        val textName = KTextView { withId(R.id.textNameTopic) }
        val textMessageCount = KTextView { withId(R.id.textMessageCount) }
    }
}