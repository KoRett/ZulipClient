package com.korett.zulip_client.di.module

import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.presentation.tabs.own_profile.OwnProfileActor
import com.korett.zulip_client.presentation.tabs.own_profile.OwnProfileStoreFactory
import com.korett.zulip_client.presentation.tabs.own_profile.mapper.OwnProfileStateMapper
import com.korett.zulip_client.presentation.tabs.own_profile.mapper.OwnProfileStateMapperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface OwnProfileModule {

    @Binds
    fun bindOwnProfileStateMapper(ownProfileStateMapperImpl: OwnProfileStateMapperImpl): OwnProfileStateMapper

    companion object {
        @Provides
        fun provideOwnProfileStoreFactory(ownProfileActor: OwnProfileActor): OwnProfileStoreFactory {
            return OwnProfileStoreFactory(ownProfileActor)
        }

        @Provides
        fun provideOwnProfileActor(userRepository: UserRepository): OwnProfileActor {
            return OwnProfileActor(userRepository)
        }
    }
}