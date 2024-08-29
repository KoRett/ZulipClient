package com.korett.zulip_client.presentation.another_profile

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
import com.korett.zulip_client.databinding.FragmentAnotherProfileBinding
import com.korett.zulip_client.databinding.FragmentOwnProfileShimmerBinding
import com.korett.zulip_client.di.component.DaggerAnotherProfileComponent
import com.korett.zulip_client.presentation.another_profile.mapper.AnotherProfileStateMapper
import com.korett.zulip_client.presentation.another_profile.state.AnotherProfileState
import com.korett.zulip_client.presentation.another_profile.state.AnotherProfileStateUi
import com.korett.zulip_client.presentation.app.App
import com.korett.zulip_client.presentation.app.appComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class AnotherProfileFragment : BaseFragment<
        AnotherProfileEffect,
        AnotherProfileState,
        AnotherProfileEvent>(R.layout.fragment_another_profile) {

    private val binding: FragmentAnotherProfileBinding by viewBinding()
    private val bindingShimmer: FragmentOwnProfileShimmerBinding by viewBinding()
    private val bindingError: ErrorLoadingDataBinding by viewBinding()

    @Inject
    lateinit var anotherProfileStoreFactory: AnotherProfileStoreFactory
    override val store: Store<
            AnotherProfileEvent,
            AnotherProfileEffect,
            AnotherProfileState> by elmStoreWithRenderer(elmRenderer = this) {
        anotherProfileStoreFactory.create()
    }

    @Inject
    lateinit var anotherProfileStateMapper: AnotherProfileStateMapper

    private val router get() = App.INSTANCE.router

    private val userId get() = requireArguments().getInt(ARG_USER_ID)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val anotherProfileComponent =
            DaggerAnotherProfileComponent.factory().create(context.appComponent)
        anotherProfileComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            store.accept(AnotherProfileEvent.Ui.Init(userId))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            hideAllStates()
        }

        binding.toolbarAnotherProfile.setNavigationOnClickListener {
            router.exit()
        }

        bindingError.buttonRefresh.setOnClickListener {
            store.accept(AnotherProfileEvent.Ui.Init(userId))
        }
    }

    override fun render(state: AnotherProfileState) {
        lifecycleScope.launch {
            val stateUi: AnotherProfileStateUi
            withContext(Dispatchers.Default) { stateUi = anotherProfileStateMapper(state) }
            renderUi(stateUi)
        }
    }

    private fun renderUi(state: AnotherProfileStateUi) {
        when (state.profile) {
            is LceState.Error -> {
                bindingShimmer.shimmerContainer.isVisible = false
                changeSuccessStateVisibility(false)

                changeErrorStateVisibility(true)
            }

            LceState.Loading -> {
                changeSuccessStateVisibility(false)
                changeErrorStateVisibility(false)

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

    companion object {

        private const val ARG_USER_ID = "user_id"

        fun newInstance(userId: Int) = AnotherProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_USER_ID, userId)
            }
        }

    }

}