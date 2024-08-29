package com.korett.zulip_client.di.module

import com.korett.zulip_client.core.domain.repository.StreamRepository
import com.korett.zulip_client.presentation.tabs.streams.StreamsActor
import com.korett.zulip_client.presentation.tabs.streams.StreamsStoreFactory
import dagger.Module
import dagger.Provides

@Module
interface StreamsModule {

    companion object {
        @Provides
        fun provideStreamsStoreFactory(streamsActor: StreamsActor): StreamsStoreFactory {
            return StreamsStoreFactory(streamsActor)
        }

        @Provides
        fun provideStreamsActor(streamRepository: StreamRepository): StreamsActor {
            return StreamsActor(streamRepository)
        }
    }
}