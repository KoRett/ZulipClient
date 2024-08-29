package com.korett.zulip_client.presentation.chat.adapter

import android.view.LayoutInflater
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.extension.getLastChild
import com.korett.zulip_client.core.ui.model.ReactionModelUi
import com.korett.zulip_client.core.ui.view.EmojiView
import com.korett.zulip_client.core.ui.view.FlexBoxLayout

fun FlexBoxLayout.addButton(onClickListener: () -> Unit) {
    LayoutInflater.from(context).inflate(R.layout.button_add_emoji_reaction, this)
    getLastChild().setOnClickListener {
        onClickListener()
    }
}

fun FlexBoxLayout.addEmoji(reactionModel: ReactionModelUi, onClickListener: () -> Unit) {
    LayoutInflater.from(context).inflate(R.layout.reaction_emoji, this)
    val emojiView = getLastChild() as EmojiView
    with(emojiView) {
        emoji = reactionModel.reactionCode
        count = reactionModel.count
        isSelected = reactionModel.isSelected
    }

    getLastChild().setOnClickListener {
        onClickListener()
    }
}