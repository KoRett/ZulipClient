package com.korett.zulip_client.core.ui.mapper

import com.korett.zulip_client.core.domain.model.ReactionModel
import com.korett.zulip_client.core.ui.model.ReactionModelUi

fun ReactionModel.toReactionUi(): ReactionModelUi =
    ReactionModelUi(
        reactionCode = reactionCode,
        count = count,
        isSelected = isSelected
    )