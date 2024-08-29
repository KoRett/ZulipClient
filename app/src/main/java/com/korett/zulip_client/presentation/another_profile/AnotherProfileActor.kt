package com.korett.zulip_client.presentation.another_profile

import com.korett.zulip_client.core.common.extension.runCatchingNonCancellation
import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.core.ui.mapper.toUserProfileUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor

class AnotherProfileActor(private val userRepository: UserRepository) :
    Actor<AnotherProfileCommand, AnotherProfileEvent>() {
    override fun execute(command: AnotherProfileCommand): Flow<AnotherProfileEvent> =
        when (command) {
            is AnotherProfileCommand.LoadAnotherUserData -> loadAnotherUserData(command)
        }

    private fun loadAnotherUserData(
        command: AnotherProfileCommand.LoadAnotherUserData
    ): Flow<AnotherProfileEvent.Internal> =
        flow {
            runCatchingNonCancellation {
                emit(AnotherProfileEvent.Internal.AnotherDataLoading)
                userRepository.getUserDataById(command.userId).toUserProfileUi()
            }.fold(
                onSuccess = { emit(AnotherProfileEvent.Internal.AnotherDataLoaded(it)) },
                onFailure = { emit(AnotherProfileEvent.Internal.AnotherDataLoadError(it)) }
            )
        }
}