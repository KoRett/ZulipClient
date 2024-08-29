package com.korett.zulip_client.presentation.tabs.streams.stream_tabs.mapper

import com.korett.zulip_client.core.ui.model.TopicState
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.core.ui.utils.adapter.DelegateItem
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.shimmer.topic.TOPIC_SHIMMER_ITEMS
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state.StreamTabState
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state.StreamTabStateUi
import javax.inject.Inject

interface StreamTabStateMapper : (StreamTabState) -> StreamTabStateUi

class StreamTabStateMapperImpl @Inject constructor() : StreamTabStateMapper {
    override fun invoke(state: StreamTabState): StreamTabStateUi {
        return when (val streams = state.streams) {
            is LceState.Content -> {
                val streamTabItems = mutableListOf<DelegateItem>()
                streams.data.forEach { stream ->
                    streamTabItems.add(stream.toStreamDelegate())
                    if (stream.isOpen) {
                        when (val topics = stream.topics) {
                            is TopicState.Content -> streamTabItems.addAll(topics.data.map { it.toTopicDelegate() })
                            TopicState.Loading -> streamTabItems.addAll(TOPIC_SHIMMER_ITEMS)
                        }
                    }
                }

                StreamTabStateUi(LceState.Content(streamTabItems.toList()))
            }

            is LceState.Error -> StreamTabStateUi(streams)
            is LceState.Loading -> StreamTabStateUi(streams)
        }
    }
}