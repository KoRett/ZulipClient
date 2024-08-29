package com.korett.zulip_client.di.module

import com.korett.zulip_client.core.domain.repository.StreamRepository
import com.korett.zulip_client.core.domain.use_cases.SearchStreamsUseCase
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.StreamTabActor
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.StreamTabStoreFactory
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.mapper.StreamTabStateMapper
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.mapper.StreamTabStateMapperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface StreamTabModule {

    @Binds
    fun bindStreamTabStateMapper(streamTabStateMapperImpl: StreamTabStateMapperImpl): StreamTabStateMapper

    companion object {
        @Provides
        fun provideStreamTabStoreFactory(streamTabActor: StreamTabActor): StreamTabStoreFactory {
            return StreamTabStoreFactory(streamTabActor)
        }

        @Provides
        fun provideStreamTabActor(
            streamRepository: StreamRepository,
            searchStreamsUseCase: SearchStreamsUseCase
        ): StreamTabActor {
            return StreamTabActor(streamRepository, searchStreamsUseCase)
        }
    }
}