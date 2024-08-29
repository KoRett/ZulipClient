package com.korett.zulip_client.presentation.tabs.streams.stream_tabs

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.utils.BaseFragment
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.databinding.ErrorLoadingDataBinding
import com.korett.zulip_client.databinding.FragmentStreamTabBinding
import com.korett.zulip_client.di.component.DaggerStreamTabComponent
import com.korett.zulip_client.presentation.app.App
import com.korett.zulip_client.presentation.app.appComponent
import com.korett.zulip_client.presentation.navigation.Screens
import com.korett.zulip_client.presentation.tabs.streams.StreamsSharedViewModel
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.StreamsAdapter
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.shimmer.stream.STREAM_SHIMMER_ITEMS
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.shimmer.stream.StreamShimmerDelegate
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.shimmer.topic.TopicShimmerDelegate
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.stream.StreamDelegate
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.adapter.topic.TopicDelegate
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.mapper.StreamTabStateMapper
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state.StreamTabState
import com.korett.zulip_client.presentation.tabs.streams.stream_tabs.state.StreamTabStateUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import javax.inject.Provider


class StreamTabFragment : BaseFragment<
        StreamTabEffect,
        StreamTabState,
        StreamTabEvent
        >(R.layout.fragment_stream_tab) {

    private val binding: FragmentStreamTabBinding by viewBinding()
    private val bindingError: ErrorLoadingDataBinding by viewBinding()

    @Inject
    lateinit var streamTabStateMapper: StreamTabStateMapper

    @Inject
    lateinit var streamTabStoreFactory: Provider<StreamTabStoreFactory>

    override val store: Store<StreamTabEvent,
            StreamTabEffect,
            StreamTabState> by elmStoreWithRenderer(elmRenderer = this) {
        streamTabStoreFactory.get().create(isSubscribed, streamsSharedViewModel.searchQuery.value)
    }

    private val streamsSharedViewModel: StreamsSharedViewModel by activityViewModels()

    private var adapter: StreamsAdapter? = null

    private val isSubscribed get() = requireArguments().getBoolean(ARG_IS_SUBSCRIBED)

    private val router get() = App.INSTANCE.router

    private var streamOptionView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val streamTabComponent =
            DaggerStreamTabComponent.factory().create(requireActivity().appComponent)
        streamTabComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            hideAllStates()
        }

        initStreamAdapter()
        initSearchCommandPublisher()

        store.accept(StreamTabEvent.Ui.InitStreams)

        bindingError.buttonRefresh.setOnClickListener {
            store.accept(StreamTabEvent.Ui.RetrySearchStreams)
        }
    }

    private fun initSearchCommandPublisher() {
        lifecycleScope.launch {
            streamsSharedViewModel.searchQuery.collect {
                store.accept(StreamTabEvent.Ui.SearchStreams(it))
            }
        }
    }

    private fun initStreamAdapter() {
        adapter = StreamsAdapter().apply {
            addDelegate(
                StreamDelegate(
                    onClickListener = { streamId, streamName ->
                        store.accept(StreamTabEvent.Ui.ChangeStreamOpenState(streamId, streamName))
                    },
                    onOptionClickListener = { view, streamModel ->
                        streamOptionView = view
                        store.accept(
                            StreamTabEvent.Ui.ShowStreamOptions(
                                streamModel.isSubscribed,
                                streamModel.name
                            )
                        )
                    })
            )
            addDelegate(
                TopicDelegate {
                    store.accept(StreamTabEvent.Ui.OpenTopicChat(it.streamName, it.name, it.color))
                }
            )
            addDelegate(StreamShimmerDelegate())
            addDelegate(TopicShimmerDelegate())
        }

        binding.recyclerStreams.adapter = adapter
    }

    override fun render(state: StreamTabState) {
        lifecycleScope.launch {
            val stateUi: StreamTabStateUi
            withContext(Dispatchers.Default) { stateUi = streamTabStateMapper(state) }
            renderUi(stateUi)
        }
    }

    private fun renderUi(state: StreamTabStateUi) {
        when (val streamTabDelegateItems = state.streamTabDelegateItems) {
            is LceState.Error -> {
                binding.recyclerStreams.isVisible = false
                changeErrorStateVisibility(true)
            }

            LceState.Loading -> {
                adapter?.submitList(STREAM_SHIMMER_ITEMS)

                changeErrorStateVisibility(false)
                binding.recyclerStreams.isVisible = true
            }

            is LceState.Content -> {
                adapter?.submitList(streamTabDelegateItems.data)

                changeErrorStateVisibility(false)
                binding.recyclerStreams.isVisible = true
            }
        }
    }

    private fun hideAllStates() {
        changeErrorStateVisibility(false)
        binding.recyclerStreams.isVisible = false
    }

    private fun changeErrorStateVisibility(isVisible: Boolean) {
        bindingError.textError.isVisible = isVisible
        bindingError.buttonRefresh.isVisible = isVisible
        bindingError.imageError.isVisible = isVisible
    }

    override fun handleEffect(effect: StreamTabEffect) = when (effect) {
        is StreamTabEffect.ErrorSubscribing -> showToastErrorStreamSubscribing()
        is StreamTabEffect.ErrorUnsubscribing -> showToastErrorStreamUnsubscribing()
        is StreamTabEffect.OpenTopicChat -> openTopicChat(effect)
        is StreamTabEffect.ShowSubscribedOptions -> tryShowSubscribedOptions(effect)
        is StreamTabEffect.ShowUnsubscribedOptions -> tryShowUnsubscribedPopupMenu(effect)
    }

    private fun openTopicChat(effect: StreamTabEffect.OpenTopicChat) {
        router.navigateTo(Screens.Chat(effect.streamName, effect.topicName, effect.color))
    }

    private fun showToastErrorStreamSubscribing() {
        Toast.makeText(
            requireContext(),
            getString(R.string.error_stream_subscribing),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showToastErrorStreamUnsubscribing() {
        Toast.makeText(
            requireContext(),
            getString(R.string.error_stream_unsubscribing),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun tryShowSubscribedOptions(effect: StreamTabEffect.ShowSubscribedOptions) {
        if (streamOptionView != null) {
            showSubscribedOptions(streamOptionView!!, effect)
        } else {
            showErrorMenuOpening()
        }
    }

    private fun showSubscribedOptions(view: View, effect: StreamTabEffect.ShowSubscribedOptions) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_subscribed)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemUnsubscribe -> {
                    store.accept(StreamTabEvent.Ui.UnsubscribeFromStream(effect.streamName))
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun tryShowUnsubscribedPopupMenu(effect: StreamTabEffect.ShowUnsubscribedOptions) {
        if (streamOptionView != null) {
            showUnsubscribedPopupMenu(streamOptionView!!, effect)
        } else {
            showErrorMenuOpening()
        }
    }

    private fun showUnsubscribedPopupMenu(
        view: View,
        effect: StreamTabEffect.ShowUnsubscribedOptions
    ) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_unsubscribed)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemSubscribe -> {
                    store.accept(StreamTabEvent.Ui.SubscribeToStream(effect.streamName))
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showErrorMenuOpening() {
        Toast.makeText(
            context,
            getString(R.string.error_menu_opening),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {

        private const val ARG_IS_SUBSCRIBED = "ARG_IS_SUBSCRIBED"

        @JvmStatic
        fun newInstance(isSubscribed: Boolean) =
            StreamTabFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_IS_SUBSCRIBED, isSubscribed)
                }
            }
    }
}