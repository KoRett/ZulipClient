package com.korett.zulip_client.presentation.tabs

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korett.zulip_client.R
import com.korett.zulip_client.databinding.FragmentTabBinding
import com.korett.zulip_client.presentation.navigation.Screens

class TabFragment : Fragment(R.layout.fragment_tab) {

    private val binding: FragmentTabBinding by viewBinding()

    private sealed class TabScreens(val tag: String) {
        data object Streams : TabScreens(TAG_FRAGMENT_STREAMS)
        data object People : TabScreens(TAG_FRAGMENT_PEOPLE)
        data object OwnProfile : TabScreens(TAG_FRAGMENT_OWN_PROFILE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            selectTab(TabScreens.Streams)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.status_bar_color)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuStreamsItem -> {
                    selectTab(TabScreens.Streams)
                    true
                }

                R.id.menuPeopleItem -> {
                    selectTab(TabScreens.People)
                    true
                }

                R.id.menuProfileItem -> {
                    selectTab(TabScreens.OwnProfile)
                    true
                }

                else -> false
            }
        }
    }

    private fun selectTab(tab: TabScreens) {
        var currentFragment: Fragment? = null
        childFragmentManager.fragments.forEach { fragment ->
            if (fragment.isVisible && fragment.tag != this.tag) {
                currentFragment = fragment
                return@forEach
            }
        }

        val newFragment = childFragmentManager.findFragmentByTag(tab.tag)

        if (currentFragment != null && newFragment != null && currentFragment === newFragment) return

        val transaction = childFragmentManager.beginTransaction()
        if (newFragment == null) {
            when (tab) {
                TabScreens.People -> {
                    transaction.add(
                        R.id.tabFragmentContainer,
                        Screens.People().createFragment(childFragmentManager.fragmentFactory),
                        tab.tag
                    )
                }

                TabScreens.OwnProfile -> {
                    transaction.add(
                        R.id.tabFragmentContainer,
                        Screens.OwnProfile().createFragment(childFragmentManager.fragmentFactory),
                        tab.tag
                    )
                }

                TabScreens.Streams -> {
                    transaction.add(
                        R.id.tabFragmentContainer,
                        Screens.Streams().createFragment(childFragmentManager.fragmentFactory),
                        tab.tag
                    )
                }
            }
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment!!)
        }

        if (newFragment != null) {
            transaction.show(newFragment)
        }
        transaction.commitNow()
    }

    companion object {
        private const val TAG_FRAGMENT_STREAMS = "TAG_FRAGMENT_STREAMS"
        private const val TAG_FRAGMENT_PEOPLE = "TAG_FRAGMENT_PEOPLE"
        private const val TAG_FRAGMENT_OWN_PROFILE = "TAG_FRAGMENT_PROFILE"
    }
}