package com.korett.zulip_client.presentation.tabs.streams.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.StreamTabFragment

class StreamTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> StreamTabFragment.newInstance(true)
        1 -> StreamTabFragment.newInstance(false)
        else -> throw IllegalArgumentException()
    }
}