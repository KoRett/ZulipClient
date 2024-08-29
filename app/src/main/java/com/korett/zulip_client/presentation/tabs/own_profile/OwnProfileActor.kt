package com.korett.zulip_client.presentation.tabs.own_profile

import com.korett.zulip_client.core.common.extension.runCatchingNonCancellation
import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.core.ui.mapper.toUserProfileUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class OwnProfileActor(private val userRepository: UserRepository) :
    Actor<OwnProfileCommand, OwnProfileEvent>() {
    override fun execute(command: OwnProfileCommand): Flow<OwnProfileEvent> = when (command) {
        OwnProfileCommand.LoadOwnUserData -> loadOwnUserData()
    }

    private fun loadOwnUserData(): Flow<OwnProfileEvent.Internal> = flow {
        emit(OwnProfileEvent.Internal.OwnDataLoading)
        while (true) {
            runCatchingNonCancellation {
                userRepository.getOwnUserData().toUserProfileUi()
            }.fold(
                onSuccess = { emit(OwnProfileEvent.Internal.OwnDataLoaded(it)) },
                onFailure = { emit(OwnProfileEvent.Internal.OwnDataLoadError(it)) }
            )
            delay(DATA_LOAD_DELAY_MILLIS)
        }
    }

    companion object {
        private const val DATA_LOAD_DELAY_MILLIS = 5000L
    }
}