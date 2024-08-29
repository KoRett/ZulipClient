package com.korett.zulip_client.core.domain.use_cases

import com.korett.zulip_client.core.domain.model.StreamModel
import com.korett.zulip_client.core.domain.repository.StreamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchStreamsUseCase @Inject constructor(
    private val streamRepository: StreamRepository
) {
    operator fun invoke(isSubscribed: Boolean, query: String): Flow<List<StreamModel>> =
        if (isSubscribed) {
            streamRepository.getSubscribedStreams()
        } else {
            streamRepository.getAllStreams()
        }
            .map { streamModels ->
                streamModels.filter { streamModel ->
                    query.lowercase() in streamModel.name.lowercase()
                }
            }
}