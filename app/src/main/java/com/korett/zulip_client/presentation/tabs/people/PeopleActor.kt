package com.korett.zulip_client.presentation.tabs.people

import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.core.domain.use_cases.SearchUsersUseCase
import com.korett.zulip_client.core.ui.mapper.toUserModelUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.switcher.Switcher

class PeopleActor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val userRepository: UserRepository
) : Actor<PeopleCommand, PeopleEvent>() {

    private val usersSwitcher = Switcher()

    override fun execute(command: PeopleCommand): Flow<PeopleEvent> = when (command) {
        is PeopleCommand.SearchPeople -> usersSwitcher.switch(DEBOUNCE_MILLIS) {
            getPeople(command)
        }
    }

    private fun getPeople(command: PeopleCommand.SearchPeople): Flow<PeopleEvent.Internal> = flow {
        if (command.query.isBlank()) {
            userRepository.getAllUsers()
        } else {
            searchUsersUseCase(command.query)
        }
            .map { it.sortedBy { user -> user.username } }
            .map { it.map { user -> user.toUserModelUi() } }
            .catch {
                emit(PeopleEvent.Internal.UsersLoadError(it))
            }
            .collect {
                emit(PeopleEvent.Internal.UsersLoaded(it))
            }
    }

    companion object {
        private const val DEBOUNCE_MILLIS = 500L
    }
}