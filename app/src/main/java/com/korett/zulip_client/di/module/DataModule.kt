package com.korett.zulip_client.di.module

import android.content.Context
import androidx.room.Room
import com.korett.zulip_client.BuildConfig
import com.korett.zulip_client.core.data.database.AppDatabase
import com.korett.zulip_client.core.data.database.dao.MessageDao
import com.korett.zulip_client.core.data.database.dao.ReactionDao
import com.korett.zulip_client.core.data.database.dao.StreamDao
import com.korett.zulip_client.core.data.database.dao.TopicDao
import com.korett.zulip_client.core.data.database.dao.UserDao
import com.korett.zulip_client.core.data.local_storage.LocalStorage
import com.korett.zulip_client.core.data.local_storage.LocalStorageImpl
import com.korett.zulip_client.core.data.remote_source.AuthorizationHeaderInterceptor
import com.korett.zulip_client.core.data.remote_source.ZulipRemoteSource
import com.korett.zulip_client.core.data.repository.ChatRepositoryImpl
import com.korett.zulip_client.core.data.repository.StreamRepositoryImpl
import com.korett.zulip_client.core.data.repository.UserRepositoryImpl
import com.korett.zulip_client.core.domain.repository.ChatRepository
import com.korett.zulip_client.core.domain.repository.StreamRepository
import com.korett.zulip_client.core.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
interface DataModule {

    @Binds
    fun bindLocalStorage(localSourceImpl: LocalStorageImpl): LocalStorage

    @Binds
    fun bindMessageRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    fun bindStreamRepository(streamRepositoryImpl: StreamRepositoryImpl): StreamRepository

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    companion object {
        @Provides
        @Singleton
        fun provideZulipRemoteSource(client: OkHttpClient): ZulipRemoteSource {
            val mediaType = "application/json; charset=UTF8".toMediaType()
            val jsonSerializer = Json { ignoreUnknownKeys = true }

            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(jsonSerializer.asConverterFactory(mediaType))
                .client(client)
                .build()
                .create()
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient
                .Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    }
                )
                .addInterceptor(AuthorizationHeaderInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }

        @Provides
        @Singleton
        fun provideAppDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                name = "app_database.db"
            ).build()
        }

        @Provides
        fun provideUserDAO(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

        @Provides
        fun provideStreamDAO(appDatabase: AppDatabase): StreamDao = appDatabase.streamDao()

        @Provides
        fun provideMessageDAO(appDatabase: AppDatabase): MessageDao = appDatabase.messageDao()

        @Provides
        fun provideTopicDAO(appDatabase: AppDatabase): TopicDao = appDatabase.topicDao()

        @Provides
        fun provideReactionDao(appDatabase: AppDatabase): ReactionDao = appDatabase.reactionDao()
    }
}