package com.korett.zulip_client.presentation.navigation

import androidx.annotation.ColorInt
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.korett.zulip_client.presentation.another_profile.AnotherProfileFragment
import com.korett.zulip_client.presentation.chat.ChatFragment
import com.korett.zulip_client.presentation.tabs.TabFragment
import com.korett.zulip_client.presentation.tabs.people.PeopleFragment
import com.korett.zulip_client.presentation.tabs.own_profile.OwnProfileFragment
import com.korett.zulip_client.presentation.tabs.streams.StreamsFragment


object Screens {
    fun Tab() = FragmentScreen { TabFragment() }
    fun Chat(streamName: String, topicName: String, @ColorInt color: Int) =
        FragmentScreen { ChatFragment.newInstance(streamName, topicName, color) }

    fun Streams() = FragmentScreen { StreamsFragment() }
    fun People() = FragmentScreen { PeopleFragment() }
    fun OwnProfile() = FragmentScreen { OwnProfileFragment() }
    fun AnotherProfile(userId: Int) =
        FragmentScreen { AnotherProfileFragment.newInstance(userId) }
}