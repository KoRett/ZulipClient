package com.korett.zulip_client.di.module

import com.korett.zulip_client.core.domain.repository.UserRepository
import com.korett.zulip_client.presentation.another_profile.AnotherProfileActor
import com.korett.zulip_client.presentation.another_profile.AnotherProfileStoreFactory
import com.korett.zulip_client.presentation.another_profile.mapper.AnotherProfileStateMapper
import com.korett.zulip_client.presentation.another_profile.mapper.AnotherProfileStateMapperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface AnotherProfileModule {

    @Binds
    fun bindAnotherProfileMapper(anotherProfileStateMapperImpl: AnotherProfileStateMapperImpl): AnotherProfileStateMapper

    companion object {
        @Provides
        fun provideAnotherProfileStoreFactory(anotherProfileActor: AnotherProfileActor): AnotherProfileStoreFactory {
            return AnotherProfileStoreFactory(anotherProfileActor)
        }

        @Provides
        fun providesAnotherProfileActor(userRepository: UserRepository): AnotherProfileActor {
            return AnotherProfileActor(userRepository)
        }
    }
}