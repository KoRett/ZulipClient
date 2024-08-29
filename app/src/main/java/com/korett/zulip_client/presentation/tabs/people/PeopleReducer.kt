package com.korett.zulip_client.presentation.tabs.people

import com.korett.zulip_client.core.ui.model.UserModelUi
import com.korett.zulip_client.core.ui.model.UserStatusUi
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.presentation.tabs.people.state.PeopleState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class PeopleReducer : ScreenDslReducer<
        PeopleEvent,
        PeopleEvent.Ui,
        PeopleEvent.Internal,
        PeopleState,
        PeopleEffect,
        PeopleCommand
        >(PeopleEvent.Ui::class, PeopleEvent.Internal::class) {
    override fun Result.internal(event: PeopleEvent.Internal): Any = when (event) {
        PeopleEvent.Internal.UsersLoading -> state {
            copy(users = LceState.Loading)
        }

        is PeopleEvent.Internal.UsersLoadError -> {
            state { copy(users = LceState.Error(event.throwable)) }
        }

        is PeopleEvent.Internal.UsersLoaded -> {
            when (val peopleState = state.users) {
                is LceState.Content -> {
                    state {
                        copy(
                            users = LceState.Content(
                                comparePeople(peopleState.data, event.users)
                            )
                        )
                    }
                }

                LceState.Loading -> {
                    state { copy(users = LceState.Content(event.users)) }
                }

                is LceState.Error -> Unit
            }
        }
    }

    private fun comparePeople(
        currentPeople: List<UserModelUi>,
        newPeople: List<UserModelUi>
    ): List<UserModelUi> = newPeople.map { user ->
        val existedUser = currentPeople.firstOrNull { it.id == user.id }
        if (existedUser != null) {
            if (user.status == UserStatusUi.Unknown && existedUser.status != UserStatusUi.Unknown) {
                user.copy(status = existedUser.status)
            } else {
                user
            }
        } else {
            user
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui): Any = when (event) {
        PeopleEvent.Ui.InitPeople -> {
            commands { +PeopleCommand.SearchPeople(state.lastQuery) }
        }

        is PeopleEvent.Ui.SearchPeople -> {
            if (state.lastQuery != event.query) {
                commands { +PeopleCommand.SearchPeople(event.query) }
                state { copy(lastQuery = event.query) }
            } else Unit
        }

        is PeopleEvent.Ui.RetrySearchPeople -> {
            state { copy(users = LceState.Loading, lastQuery = event.query) }
            commands { +PeopleCommand.SearchPeople(event.query) }
        }

        is PeopleEvent.Ui.NavigateToAnotherUserProfile -> {
            effects {
                +PeopleEffect.NavigateToAnotherUserProfile(event.userId)
            }
        }
    }
}