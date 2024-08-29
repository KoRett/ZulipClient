package com.korett.zulip_client.core.data.remote_source.mapper

import com.korett.zulip_client.core.data.remote_source.model.ReactionModelRemote
import com.korett.zulip_client.core.domain.model.ReactionModel

private data class Reaction(var count: Int, val isSelected: Boolean)

private fun isSelectedReactionByUser(
    reactionsRemote: List<ReactionModelRemote>,
    usersOwnId: Int,
    reactionCode: String
): Boolean {
    return reactionsRemote.find { it.userId == usersOwnId && it.emojiCode == reactionCode } != null
}

private fun fromReactionCodeToUnicode(reactionCode: String): String {
    var unicode = ""
    try {
        val unicodeList = reactionCode.split("-")
        unicodeList.forEach { unicode += String(Character.toChars(it.toInt(16))) }
    } catch (e: Exception) {
        unicode = "?"
    }
    return unicode
}

fun mapReactionsRemoteToSortedReactionsDomain(
    reactionsRemote: List<ReactionModelRemote>,
    usersOwnId: Int
): List<ReactionModel> {
    val reactions = mutableMapOf<String, Reaction>()

    reactionsRemote.forEach { remoteReaction ->
        val reaction = reactions.getOrPut(remoteReaction.emojiCode) {
            Reaction(
                count = 0,
                isSelected = isSelectedReactionByUser(
                    reactionsRemote,
                    usersOwnId,
                    remoteReaction.emojiCode
                )
            )
        }
        reaction.count++
    }

    val reactionsDomain = reactions.map { entry ->
        ReactionModel(
            fromReactionCodeToUnicode(entry.key),
            entry.value.count,
            entry.value.isSelected
        )
    }

    return reactionsDomain.sortedByDescending { it.count }
}