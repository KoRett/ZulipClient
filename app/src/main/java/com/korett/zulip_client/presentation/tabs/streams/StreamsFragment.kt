package com.korett.zulip_client.presentation.tabs.streams

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.utils.BaseFragment
import com.korett.zulip_client.databinding.FragmentStreamsBinding
import com.korett.zulip_client.di.component.DaggerStreamsComponent
import com.korett.zulip_client.presentation.app.appComponent
import com.korett.zulip_client.presentation.tabs.streams.adapter.StreamTabAdapter
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import javax.inject.Provider

class StreamsFragment : BaseFragment<
        StreamsEffect,
        Unit,
        StreamsEvent
        >(R.layout.fragment_streams) {

    private val binding: FragmentStreamsBinding by viewBinding()

    private var adapter: StreamTabAdapter? = null

    private val streamsSharedViewModel: StreamsSharedViewModel by activityViewModels()

    @Inject
    lateinit var streamsStoreFactory: Provider<StreamsStoreFactory>

    override val store: Store<StreamsEvent,
            StreamsEffect,
            Unit> by elmStoreWithRenderer(elmRenderer = this) {
        streamsStoreFactory.get().create()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val streamTabComponent =
            DaggerStreamsComponent.factory().create(requireActivity().appComponent)
        streamTabComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            streamsSharedViewModel.searchQuery.tryEmit(DEFAULT_QUERY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initCreateStreamDialogResultListener()

        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            streamsSharedViewModel.searchQuery.tryEmit(text.toString())
        }

        binding.buttonOptions.setOnClickListener {
            store.accept(StreamsEvent.Ui.ShowOptions)
        }
    }

    override fun render(state: Unit) = Unit

    private fun initAdapter() {
        adapter = StreamTabAdapter(this)

        binding.viewPagerStreamTabs.adapter = adapter

        TabLayoutMediator(binding.tabContainer, binding.viewPagerStreamTabs) { tab, position ->
            tab.text = when (position) {
                SUBSCRIBED_POSITION -> getString(R.string.subscribed)
                ALL_STREAMS_POSITION -> getString(R.string.all_streams)
                else -> throw IllegalArgumentException()
            }

        }.attach()
    }

    private fun initCreateStreamDialogResultListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            CreateStreamDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val streamName = bundle.getString(CreateStreamDialogFragment.STREAM_NAME_KEY)
            store.accept(StreamsEvent.Ui.CreateStream(streamName))
        }
    }

    override fun handleEffect(effect: StreamsEffect) = when (effect) {
        StreamsEffect.StreamAlreadyExist -> showToastStreamAlreadyExist()
        StreamsEffect.ShowOptions -> showOptions()
        StreamsEffect.ShowStreamCreation -> showStreamCreation()
        is StreamsEffect.StreamCreationError -> showToastStreamCreationError()
    }

    private fun showOptions() {
        val popupMenu = PopupMenu(requireContext(), binding.buttonOptions)
        popupMenu.inflate(R.menu.menu_stream_options)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itemCreateStream -> {
                    store.accept(StreamsEvent.Ui.ShowStreamCreation)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showStreamCreation() {
        CreateStreamDialogFragment().show(childFragmentManager, CreateStreamDialogFragment.TAG)
    }

    private fun showToastStreamCreationError() {
        Toast.makeText(
            requireContext(),
            getString(R.string.stream_creation_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showToastStreamAlreadyExist() {
        Toast.makeText(
            requireContext(),
            getString(R.string.stream_already_exist),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {
        private const val SUBSCRIBED_POSITION = 0
        private const val ALL_STREAMS_POSITION = 1

        private const val DEFAULT_QUERY = ""
    }
}