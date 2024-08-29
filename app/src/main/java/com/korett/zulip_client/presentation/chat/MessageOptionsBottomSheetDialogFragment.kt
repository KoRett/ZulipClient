package com.korett.zulip_client.presentation.chat

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.korett.zulip_client.R
import com.korett.zulip_client.databinding.BottomSheetDialogFragmentMessageOptionsBinding

class MessageOptionsBottomSheetDialogFragment private constructor() :
    BottomSheetDialogFragment(R.layout.bottom_sheet_dialog_fragment_message_options) {

    private val binding: BottomSheetDialogFragmentMessageOptionsBinding by viewBinding()
    private val messageId get() = requireArguments().getInt(ARG_MESSAGE_ID)
    private val messageContent get() = requireArguments().getString(ARG_MESSAGE_CONTENT)!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textAddReaction.setOnClickListener {
            requireActivity().supportFragmentManager.setFragmentResult(
                TAG,
                createAddReactionResult()
            )
            dismiss()
        }

        binding.textCopyMessage.setOnClickListener {
            requireActivity().supportFragmentManager.setFragmentResult(
                TAG,
                createCopyMessageResult()
            )
            dismiss()
        }
    }

    private fun createAddReactionResult() = bundleOf(
        OPTION_KEY to ADD_REACTION_OPTION,
        MESSAGE_ID_KEY to messageId,
        MESSAGE_CONTENT_KEY to messageContent
    )

    private fun createCopyMessageResult() = bundleOf(
        OPTION_KEY to COPY_MESSAGE_OPTION,
        MESSAGE_ID_KEY to messageId,
        MESSAGE_CONTENT_KEY to messageContent
    )

    companion object {
        const val TAG = "MessageOptionsBottomSheetDialogFragment"

        const val OPTION_KEY = "OPTION_KEY"
        const val ADD_REACTION_OPTION = 1
        const val COPY_MESSAGE_OPTION = 2

        const val MESSAGE_ID_KEY = "MESSAGE_ID_KEY"
        const val MESSAGE_CONTENT_KEY = "MESSAGE_CONTENT_KEY"

        private const val ARG_MESSAGE_ID = "ARG_MESSAGE_ID"
        private const val ARG_MESSAGE_CONTENT = "ARG_MESSAGE_CONTENT"

        fun getInstance(
            messageId: Int,
            messageContent: String
        ): MessageOptionsBottomSheetDialogFragment =
            MessageOptionsBottomSheetDialogFragment().apply {
                arguments = createArguments(messageId, messageContent)
            }

        fun createArguments(messageId: Int, messageContent: String) = bundleOf(
            ARG_MESSAGE_ID to messageId,
            ARG_MESSAGE_CONTENT to messageContent
        )
    }
}