package com.korett.zulip_client.presentation.tabs.own_profile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.model.UserProfileStatusUi
import com.korett.zulip_client.core.ui.utils.BaseFragment
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.databinding.ErrorLoadingDataBinding
import com.korett.zulip_client.databinding.FragmentOwnProfileBinding
import com.korett.zulip_client.databinding.FragmentOwnProfileShimmerBinding
import com.korett.zulip_client.di.component.DaggerOwnProfileComponent
import com.korett.zulip_client.presentation.app.appComponent
import com.korett.zulip_client.presentation.tabs.own_profile.mapper.OwnProfileStateMapper
import com.korett.zulip_client.presentation.tabs.own_profile.state.OwnProfileState
import com.korett.zulip_client.presentation.tabs.own_profile.state.OwnProfileStateUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class OwnProfileFragment : BaseFragment<
        OwnProfileEffect,
        OwnProfileState,
        OwnProfileEvent
        >(R.layout.fragment_own_profile) {

    private val binding: FragmentOwnProfileBinding by viewBinding()
    private val bindingShimmer: FragmentOwnProfileShimmerBinding by viewBinding()
    private val bindingError: ErrorLoadingDataBinding by viewBinding()

    @Inject
    lateinit var ownProfileStoreFactory: OwnProfileStoreFactory
    override val store: Store<
            OwnProfileEvent,
            OwnProfileEffect,
            OwnProfileState> by elmStoreWithRenderer(elmRenderer = this) {
        ownProfileStoreFactory.create()
    }

    @Inject
    lateinit var ownProfileStateMapper: OwnProfileStateMapper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val ownProfileComponent = DaggerOwnProfileComponent.factory().create(context.appComponent)
        ownProfileComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(OwnProfileEvent.Ui.Init)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            hideAllStates()
        }

        bindingError.buttonRefresh.setOnClickListener {
            store.accept(OwnProfileEvent.Ui.Init)
        }
    }

    override fun render(state: OwnProfileState) {
        lifecycleScope.launch {
            val stateUi: OwnProfileStateUi
            withContext(Dispatchers.Default) { stateUi = ownProfileStateMapper(state) }
            renderUi(stateUi)
        }
    }

    private fun renderUi(state: OwnProfileStateUi) {
        when (state.profile) {
            is LceState.Error -> {
                bindingShimmer.shimmerContainer.isVisible = false
                changeSuccessStateVisibility(false)

                changeErrorStateVisibility(true)
            }

            LceState.Loading -> {
                changeErrorStateVisibility(false)
                changeSuccessStateVisibility(false)

                bindingShimmer.shimmerContainer.isVisible = true
            }

            is LceState.Content -> {
                with(state.profile.data) {
                    setStatus(status)
                    binding.textName.text = username

                    Glide.with(requireContext())
                        .load(avatarUrl)
                        .placeholder(R.drawable.loading_placeholder)
                        .into(binding.imageAvatar)
                }

                changeErrorStateVisibility(false)
                bindingShimmer.shimmerContainer.isVisible = false

                changeSuccessStateVisibility(true)
            }
        }
    }

    private fun setStatus(status: UserProfileStatusUi) {
        with(binding.textStatus) {
            text = getString(status.text)
            setTextColor(ContextCompat.getColor(context, status.color))
        }
    }

    private fun hideAllStates() {
        changeSuccessStateVisibility(false)
        changeErrorStateVisibility(false)
        bindingShimmer.shimmerContainer.isVisible = false
    }

    private fun changeErrorStateVisibility(isVisible: Boolean) {
        bindingError.textError.isVisible = isVisible
        bindingError.buttonRefresh.isVisible = isVisible
        bindingError.imageError.isVisible = isVisible
    }

    private fun changeSuccessStateVisibility(isVisible: Boolean) {
        binding.textStatus.isVisible = isVisible
        binding.textName.isVisible = isVisible
        binding.imageAvatar.isVisible = isVisible
    }
}