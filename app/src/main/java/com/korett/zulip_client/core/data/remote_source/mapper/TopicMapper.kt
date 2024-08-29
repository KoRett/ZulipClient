package com.korett.zulip_client.core.data.remote_source.mapper

import android.graphics.Color
import com.korett.zulip_client.core.data.remote_source.model.TopicModelNetwork
import com.korett.zulip_client.core.domain.model.TopicModel


fun TopicModelNetwork.toTopicDomain(streamId: Int, streamName: String, messageCount: Int = 0) =
    TopicModel(
        streamId = streamId,
        streamName = streamName,
        name = name,
        messageCount = messageCount,
        color = name.toColor()
    )

fun String.toColor(factor: Float = 0.6f): Int {
    val hash = this.hashCode()
    val red = (((hash and 0xFF0000) * factor).toInt() shr 16)
    val green = (((hash and 0x00FF00) * factor).toInt() shr 8)
    val blue = ((hash and 0x0000FF) * factor).toInt()

    return Color.rgb(red, green, blue)
}