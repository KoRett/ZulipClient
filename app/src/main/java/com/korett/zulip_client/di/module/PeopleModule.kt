package com.korett.zulip_client.di.module

import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.core.domain.use_cases.SearchUsersUseCase
import com.korett.zulip_client.presentation.tabs.people.PeopleActor
import com.korett.zulip_client.presentation.tabs.people.PeopleStoreFactory
import com.korett.zulip_client.presentation.tabs.people.mapper.PeopleStateMapper
import com.korett.zulip_client.presentation.tabs.people.mapper.PeopleStateMapperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface PeopleModule {

    @Binds
    fun bindPeopleStateMapper(peopleStateMapperImpl: PeopleStateMapperImpl): PeopleStateMapper

    companion object {
        @Provides
        fun providePeopleStoreFactory(peopleActor: PeopleActor): PeopleStoreFactory {
            return PeopleStoreFactory(peopleActor)
        }

        @Provides
        fun providePeopleActor(
            userRepository: UserRepository,
            searchPeopleUseCase: SearchUsersUseCase
        ): PeopleActor {
            return PeopleActor(searchPeopleUseCase, userRepository)
        }
    }
}