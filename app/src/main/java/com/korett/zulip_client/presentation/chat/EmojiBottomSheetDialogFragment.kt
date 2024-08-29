package com.korett.zulip_client.presentation.chat

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.korett.zulip_client.R
import com.korett.zulip_client.databinding.BottomSheetDialogFragmentEmojiBinding

class EmojiBottomSheetDialogFragment :
    BottomSheetDialogFragment(R.layout.bottom_sheet_dialog_fragment_emoji) {

    private val binding: BottomSheetDialogFragmentEmojiBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emojiPicker.setOnEmojiPickedListener {
            requireActivity().supportFragmentManager.setFragmentResult(
                TAG,
                bundleOf(EMOJI_KEY to it.emoji)
            )
            dismiss()
        }
    }

    companion object {
        const val TAG = "EMOJI_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG"
        const val EMOJI_KEY = "EMOJI_KEY"
    }
}