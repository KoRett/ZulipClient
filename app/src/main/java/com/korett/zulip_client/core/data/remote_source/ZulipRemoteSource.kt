package com.korett.zulip_client.core.data.remote_source

import com.korett.zulip_client.core.data.remote_source.model.AllStreamsResponse
import com.korett.zulip_client.core.data.remote_source.model.AllUsersResponse
import com.korett.zulip_client.core.data.remote_source.model.AnotherUserResponse
import com.korett.zulip_client.core.data.remote_source.model.EmojiCodesModelNetwork
import com.korett.zulip_client.core.data.remote_source.model.MessageEventResponse
import com.korett.zulip_client.core.data.remote_source.model.MessageResponse
import com.korett.zulip_client.core.data.remote_source.model.MessagesResponse
import com.korett.zulip_client.core.data.remote_source.model.OwnUserDataResponse
import com.korett.zulip_client.core.data.remote_source.model.RegisterEventQueueResponse
import com.korett.zulip_client.core.data.remote_source.model.StreamResponse
import com.korett.zulip_client.core.data.remote_source.model.SubscribedStreamsResponse
import com.korett.zulip_client.core.data.remote_source.model.TopicsResponse
import com.korett.zulip_client.core.data.remote_source.model.user_presence.UserPresenceResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ZulipRemoteSource {

    @GET("api/v1/streams")
    suspend fun getAllStreams(): AllStreamsResponse

    @GET("api/v1/users/me/subscriptions")
    suspend fun getSubscribedStreams(): SubscribedStreamsResponse

    @GET("api/v1/streams/{streamId}")
    suspend fun getStreamById(@Path("streamId") streamId: Int): StreamResponse

    @GET("api/v1/users/me/{streamId}/topics")
    suspend fun getStreamTopics(@Path("streamId") streamId: Int): TopicsResponse

    @GET("api/v1/messages?num_before=0&anchor=first_unread&num_after=301")
    suspend fun getTopicUnreadMessages(@Query("narrow") narrow: String): MessagesResponse

    @GET("api/v1/messages?num_after=0&anchor=newest")
    suspend fun getTopicNewestMessages(
        @Query("narrow") narrow: String,
        @Query("num_before") numBefore: Int
    ): MessagesResponse

    @GET("api/v1/messages?num_after=0")
    suspend fun getTopicPreviousMessages(
        @Query("anchor") messageId: Int,
        @Query("narrow") narrow: String,
        @Query("num_before") numBefore: Int
    ): MessagesResponse

    @POST("api/v1/register?event_types=[\"message\", \"reaction\"]")
    suspend fun registerMessageEventQueue(
        @Query("narrow") narrow: String,
    ): RegisterEventQueueResponse

    @DELETE("api/v1/events")
    suspend fun deleteEventQueue(
        @Query("queue_id") queueId: String
    )

    @GET("api/v1/events?event_queue_longpoll_timeout_seconds=20")
    suspend fun getEvents(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Int
    ): MessageEventResponse

    @GET("api/v1/messages/{messageId}")
    suspend fun getMessageById(
        @Path("messageId") messageId: Int
    ): MessageResponse

    @GET("api/v1/users")
    suspend fun getAllUsers(): AllUsersResponse

    @GET("api/v1/users/{user_id}")
    suspend fun getUserDataById(@Path("user_id") userId: Int): AnotherUserResponse

    @GET("api/v1/users/me")
    suspend fun getOwnData(): OwnUserDataResponse

    @GET("api/v1/users/{user_id}/presence")
    suspend fun getUserPresenceById(@Path("user_id") userId: Int): UserPresenceResponse

    @POST("api/v1/messages")
    suspend fun sentMessageToTopic(
        @Query("to") streamName: String,
        @Query("topic") topicName: String,
        @Query("content") content: String,
        @Query("type") type: String = "stream"
    )

    @POST("api/v1/messages/{messageId}/reactions")
    suspend fun addReactionToMessage(
        @Path("messageId") messageId: Int,
        @Query("emoji_name") emojiName: String,
        @Query("reaction_type") emojiType: String = "unicode_emoji",
    )

    @POST("api/v1/users/me/subscriptions")
    suspend fun subscribeToStream(
        @Query("subscriptions") subscriptions: String
    )

    @DELETE("api/v1/users/me/subscriptions")
    suspend fun unsubscribeToStream(
        @Query("subscriptions") subscriptions: String
    )

    @POST("api/v1/users/me/subscriptions?announce=false")
    suspend fun createStream(
        @Query("subscriptions") subscriptions: String
    )


    @DELETE("api/v1/messages/{messageId}/reactions?reaction_type=\"unicode_emoji\"")
    suspend fun removeReactionToMessage(
        @Path("messageId") messageId: Int,
        @Query("emoji_name") emojiName: String,
        @Query("reaction_type") emojiType: String = "unicode_emoji",
    )

    @GET("static/generated/emoji/emoji_codes.json")
    suspend fun getEmojisList(): EmojiCodesModelNetwork

    companion object {
        fun getNarrowMessageMap(streamName: String, topicName: String): String {
            return "[{\"operator\": \"stream\", \"operand\": \"$streamName\"},{\"operator\": \"topic\", \"operand\": \"$topicName\"}]"
        }

        fun getNarrowStreamAndTopic(streamName: String, topicName: String): String {
            return "[[\"stream\", \"$streamName\"], [\"topic\", \"$topicName\"]]"
        }

        fun getSubscribeSubscriptions(streamName: String): String {
            return "[{\"name\":\"$streamName\"}]"
        }

        fun getUnsubscribeSubscriptions(streamName: String): String {
            return "[\"$streamName\"]"
        }
    }

}