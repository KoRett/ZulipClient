package com.korett.zulip_client.presentation.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korett.zulip_client.R
import com.korett.zulip_client.core.ui.utils.BaseFragment
import com.korett.zulip_client.core.ui.utils.LceState
import com.korett.zulip_client.databinding.ErrorLoadingDataBinding
import com.korett.zulip_client.databinding.FragmentChatBinding
import com.korett.zulip_client.di.component.DaggerChatComponent
import com.korett.zulip_client.presentation.app.App
import com.korett.zulip_client.presentation.app.appComponent
import com.korett.zulip_client.presentation.chat.adapter.MessageAdapter
import com.korett.zulip_client.presentation.chat.adapter.another_message.AnotherMessageDelegate
import com.korett.zulip_client.presentation.chat.adapter.date.DateDelegate
import com.korett.zulip_client.presentation.chat.adapter.error.ErrorDelegate
import com.korett.zulip_client.presentation.chat.adapter.load.LoadDelegate
import com.korett.zulip_client.presentation.chat.adapter.own_message.OwnMessageDelegate
import com.korett.zulip_client.presentation.chat.mapper.ChatStateMapper
import com.korett.zulip_client.presentation.chat.state.ChatState
import com.korett.zulip_client.presentation.chat.state.ChatStateUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import java.util.Locale
import javax.inject.Inject


class ChatFragment : BaseFragment<
        ChatEffect,
        ChatState,
        ChatEvent
        >(R.layout.fragment_chat) {

    private val binding: FragmentChatBinding by viewBinding(FragmentChatBinding::bind)
    private val bindingError: ErrorLoadingDataBinding by viewBinding()

    @get:ColorInt
    private val color get() = requireArguments().getInt(ARG_COLOR)
    private val streamName get() = requireArguments().getString(ARG_STREAM_NAME)!!
    private val topicName get() = requireArguments().getString(ARG_TOPIC_NAME)!!

    private var adapter: MessageAdapter? = null

    private val router get() = App.INSTANCE.router

    @Inject
    lateinit var chatStoreFactory: ChatStoreFactory
    override val store by elmStoreWithRenderer(elmRenderer = this) {
        chatStoreFactory.create(streamName, topicName)
    }

    @Inject
    lateinit var chatStateMapper: ChatStateMapper

    private var isNeedToScrollChat = false
    private var selectedMessageView: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val chatComponent = DaggerChatComponent.factory()
            .create(context.appComponent, Locale.getDefault())
        chatComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initChatNameAndColor()
        initMessageInputField()
        initEmojiDialogResultListener()
        initMessageOptionsDialogResultListener()
        initSentButton()
        initChatAdapter()

        binding.toolbarChat.setNavigationOnClickListener {
            store.accept(ChatEvent.Ui.NavigateBack)
        }

        bindingError.buttonRefresh.setOnClickListener {
            store.accept(ChatEvent.Ui.Init)
        }

        if (savedInstanceState == null) {
            hideAllStates()
            store.accept(ChatEvent.Ui.Init)
        }
    }

    override fun onDestroyView() {
        adapter = null
        super.onDestroyView()
    }

    private fun initChatNameAndColor() {
        requireActivity().window.statusBarColor = color
        binding.toolbarChat.setBackgroundColor(color)

        binding.toolbarChat.title = getString(R.string.hash_stream_name, streamName)
        binding.textTopic.text = getString(R.string.topic_colon_hash_name, topicName)
    }

    private fun initSentButton() {
        binding.buttonSent.setOnClickListener {
            with(binding.editTextMessage.text) {
                if (isNotBlank()) {
                    store.accept(ChatEvent.Ui.SentMessage(this.toString()))
                    clear()
                }
            }
        }
    }

    private fun initEmojiDialogResultListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            EmojiBottomSheetDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val reactionCode = bundle.getString(EmojiBottomSheetDialogFragment.EMOJI_KEY)
            store.accept(ChatEvent.Ui.ReactionSelected(reactionCode))
        }
    }

    private fun initMessageInputField() {
        binding.editTextMessage.doAfterTextChanged { editable ->
            binding.buttonSent.isSelected = !editable.isNullOrBlank()
        }
    }

    private fun initChatAdapter() {
        adapter = MessageAdapter(
            onLastMessageRender = { store.accept(ChatEvent.Ui.LoadPreviousMessages) },
            MESSAGE_BUFFER_COUNT
        ).apply {
            addDelegate(
                AnotherMessageDelegate(
                    ::messageLongClick,
                    ::addReactionToMessage,
                    reactionClickListener = { messageId, reactionModel ->
                        store.accept(
                            ChatEvent.Ui.ReactionClicked(
                                messageId,
                                reactionModel.reactionCode,
                                reactionModel.isSelected
                            )
                        )
                    }
                ))
            addDelegate(
                OwnMessageDelegate(
                    ::messageLongClick,
                    ::addReactionToMessage,
                    reactionClickListener = { messageId, reactionModel ->
                        store.accept(
                            ChatEvent.Ui.ReactionClicked(
                                messageId,
                                reactionModel.reactionCode,
                                reactionModel.isSelected
                            )
                        )
                    })
            )
            addDelegate(ErrorDelegate {
                store.accept(ChatEvent.Ui.RetryLoadPreviousMessages)
            })
            addDelegate(DateDelegate())
            addDelegate(LoadDelegate())
        }

        binding.recyclerChat.adapter = adapter
    }

    private fun messageLongClick(view: View, messageId: Int, messageContent: String) {
        selectedMessageView = view
        store.accept(ChatEvent.Ui.ShowMessageOptions(messageId, messageContent))
    }

    private fun addReactionToMessage(messageId: Int) {
        store.accept(ChatEvent.Ui.SelectMessageToAddReaction(messageId))
    }

    override fun render(state: ChatState) {
        lifecycleScope.launch {
            val stateUi: ChatStateUi
            withContext(Dispatchers.Default) { stateUi = chatStateMapper(state) }
            renderUi(stateUi)
        }
    }

    private fun renderUi(state: ChatStateUi) {
        if (view != null) {
            when (state.chatItemsState) {
                is LceState.Content -> {
                    adapter?.submitList(state.chatItemsState.data) {
                        if (view != null) {
                            scrollChatToStart()
                        }
                    }
                    changeErrorStateVisibility(false)
                    binding.progressBar.isVisible = false

                    binding.recyclerChat.isVisible = true
                }

                is LceState.Error -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerChat.isVisible = false

                    changeErrorStateVisibility(true)
                }

                LceState.Loading -> {
                    binding.recyclerChat.isVisible = false
                    changeErrorStateVisibility(false)

                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun scrollChatToStart() {
        if (isNeedToScrollChat) {
            binding.recyclerChat.smoothScrollToPosition(0)
            isNeedToScrollChat = false
        }
    }

    private fun hideAllStates() {
        changeErrorStateVisibility(false)
        binding.progressBar.isVisible = false
        binding.recyclerChat.isVisible = false
    }

    private fun changeErrorStateVisibility(isVisible: Boolean) {
        bindingError.textError.isVisible = isVisible
        bindingError.buttonRefresh.isVisible = isVisible
        bindingError.imageError.isVisible = isVisible
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.ShowErrorAddingReaction -> showErrorAddingReaction()
            is ChatEffect.ShowErrorRemovingReaction -> showErrorRemovingReaction()
            is ChatEffect.ShowErrorSendingMessage -> showErrorSendingMessage()
            is ChatEffect.ShowMessageOptions -> showMessageOptions(effect)
            ChatEffect.ShowReactionSelector -> showEmojiBottomSheetDialogFragment()

            ChatEffect.ScrollChatToStart -> {
                isNeedToScrollChat = true
            }

            ChatEffect.NavigateBack -> {
                router.exit()
            }
        }
    }

    private fun showMessageOptions(effect: ChatEffect.ShowMessageOptions) {
        hideKeyboard()
        MessageOptionsBottomSheetDialogFragment.getInstance(effect.messageId, effect.messageContent)
            .show(
                this@ChatFragment.childFragmentManager,
                MessageOptionsBottomSheetDialogFragment.TAG
            )
    }

    private fun initMessageOptionsDialogResultListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            MessageOptionsBottomSheetDialogFragment.TAG,
            viewLifecycleOwner
        ) { _, bundle ->
            val optionId = bundle.getInt(MessageOptionsBottomSheetDialogFragment.OPTION_KEY)
            val messageId = bundle.getInt(MessageOptionsBottomSheetDialogFragment.MESSAGE_ID_KEY)
            val messageContent =
                bundle.getString(MessageOptionsBottomSheetDialogFragment.MESSAGE_CONTENT_KEY)!!

            when (optionId) {
                MessageOptionsBottomSheetDialogFragment.ADD_REACTION_OPTION -> {
                    addReactionToMessage(messageId)
                }

                MessageOptionsBottomSheetDialogFragment.COPY_MESSAGE_OPTION -> {
                    copyMessageContent(messageContent)
                }

                else -> Unit
            }
        }
    }

    private fun copyMessageContent(messageContent: String) {
        val clipboardManager =
            requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(MESSAGE_CONTENT, messageContent))
    }

    private fun showErrorAddingReaction() {
        Toast.makeText(
            context,
            getString(R.string.an_error_occurred_when_adding_the_reaction),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showErrorRemovingReaction() {
        Toast.makeText(
            context,
            getString(R.string.an_error_occurred_when_deleting_the_reaction),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showErrorSendingMessage() {
        Toast.makeText(
            context,
            getString(R.string.error_sending_the_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showEmojiBottomSheetDialogFragment() {
        hideKeyboard()
        EmojiBottomSheetDialogFragment().show(
            childFragmentManager,
            EmojiBottomSheetDialogFragment.TAG
        )
    }

    private fun hideKeyboard() {
        with(requireActivity()) {
            currentFocus?.let {
                val imm =
                    getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    companion object {
        private const val ARG_STREAM_NAME = "ARG_STREAM_NAME"
        private const val ARG_TOPIC_NAME = "ARG_TOPIC_NAME"
        private const val ARG_COLOR = "ARG_COLOR"

        private const val MESSAGE_BUFFER_COUNT = 5

        private const val MESSAGE_CONTENT = "MESSAGE_CONTENT"

        @JvmStatic
        fun newInstance(streamName: String, topicName: String, @ColorInt color: Int) =
            ChatFragment().apply {
                arguments = createArgs(streamName, topicName, color)
            }

        @JvmStatic
        fun createArgs(streamName: String, topicName: String, @ColorInt color: Int) =
            Bundle().apply {
                putString(ARG_STREAM_NAME, streamName)
                putString(ARG_TOPIC_NAME, topicName)
                putInt(ARG_COLOR, color)
            }
    }
}