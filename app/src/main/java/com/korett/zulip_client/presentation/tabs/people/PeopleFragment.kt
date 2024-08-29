package com.korett.zulip_client.presentation.tabs.people

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.utils.BaseFragment
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.core.ui.utils.adapter.BIG_SHIMMER_ITEMS
import com.korett.zulip_client.databinding.ErrorLoadingDataBinding
import com.korett.zulip_client.databinding.FragmentPeopleBinding
import com.korett.zulip_client.di.component.DaggerPeopleComponent
import com.korett.zulip_client.presentation.app.App
import com.korett.zulip_client.presentation.app.appComponent
import com.korett.zulip_client.presentation.navigation.Screens
import com.korett.zulip_client.presentation.tabs.people.adapter.PeopleAdapter
import com.korett.zulip_client.presentation.tabs.people.adapter.shimmer.UserShimmerDelegate
import com.korett.zulip_client.presentation.tabs.people.adapter.user.UserDelegate
import com.korett.zulip_client.presentation.tabs.people.mapper.PeopleStateMapper
import com.korett.zulip_client.presentation.tabs.people.state.PeopleState
import com.korett.zulip_client.presentation.tabs.people.state.PeopleStateUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class PeopleFragment : BaseFragment<
        PeopleEffect,
        PeopleState,
        PeopleEvent
        >(R.layout.fragment_people) {

    private val binding: FragmentPeopleBinding by viewBinding()
    private val bindingError: ErrorLoadingDataBinding by viewBinding()

    @Inject
    lateinit var peopleStateMapper: PeopleStateMapper

    @Inject
    lateinit var peopleStoreFactory: PeopleStoreFactory
    override val store: Store<PeopleEvent,
            PeopleEffect,
            PeopleState> by elmStoreWithRenderer(elmRenderer = this) {
        peopleStoreFactory.create(DEFAULT_QUERY)
    }
    private val router get() = App.INSTANCE.router

    private var adapter: PeopleAdapter? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val peopleComponent = DaggerPeopleComponent.factory().create(context.appComponent)
        peopleComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            hideAllStates()
        }

        initPeopleAdapter()

        store.accept(PeopleEvent.Ui.InitPeople)

        bindingError.buttonRefresh.setOnClickListener {
            store.accept(PeopleEvent.Ui.RetrySearchPeople(binding.editTextSearch.text.toString()))
        }

        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            store.accept(PeopleEvent.Ui.SearchPeople(text.toString()))
        }
    }

    private fun initPeopleAdapter() {
        adapter = PeopleAdapter().apply {
            addDelegate(UserDelegate {
                store.accept(PeopleEvent.Ui.NavigateToAnotherUserProfile(it.id))
            })
            addDelegate(UserShimmerDelegate())
        }

        binding.recyclerPeople.adapter = adapter
    }

    override fun render(state: PeopleState) {
        lifecycleScope.launch {
            val stateUi: PeopleStateUi
            withContext(Dispatchers.Default) { stateUi = peopleStateMapper(state) }
            renderUi(stateUi)
        }
    }

    private fun renderUi(state: PeopleStateUi) {
        when (state.users) {
            is LceState.Error -> {
                binding.recyclerPeople.isVisible = false
                changeErrorStateVisibility(true)
            }

            LceState.Loading -> {
                adapter!!.submitList(BIG_SHIMMER_ITEMS) {
                    changeErrorStateVisibility(false)
                    binding.recyclerPeople.isVisible = true
                }
            }

            is LceState.Content -> {
                adapter!!.submitList(state.users.data) {
                    changeErrorStateVisibility(false)
                    binding.recyclerPeople.isVisible = true
                }
            }
        }
    }

    private fun hideAllStates() {
        changeErrorStateVisibility(false)
        binding.recyclerPeople.isVisible = false
    }

    private fun changeErrorStateVisibility(isVisible: Boolean) {
        bindingError.textError.isVisible = isVisible
        bindingError.buttonRefresh.isVisible = isVisible
        bindingError.imageError.isVisible = isVisible
    }

    override fun handleEffect(effect: PeopleEffect) = when (effect) {
        is PeopleEffect.NavigateToAnotherUserProfile -> {
            router.navigateTo(Screens.AnotherProfile(effect.userId))
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    companion object {
        private const val DEFAULT_QUERY = ""
    }
}