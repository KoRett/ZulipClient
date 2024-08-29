package com.korett.zulip_client.presentation.tabs.streams

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.korett.zulip_client.R
import com.korett.zulip_client.databinding.DialogFragmentCreateStreamBinding

class CreateStreamDialogFragment : DialogFragment(R.layout.dialog_fragment_create_stream) {

    private val binding: DialogFragmentCreateStreamBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCancel.setOnClickListener { dismiss() }

        binding.buttonCreate.setOnClickListener {
            if (!binding.editTextStreamName.text.isNullOrBlank()) {
                sendResult()
                dismiss()
            } else {
                showToastBlankStreamName()
            }
        }

        binding.editTextStreamName.doOnTextChanged { text, _, _, _ ->
            if (text?.startsWith(" ") == true) {
                binding.editTextStreamName.setText(text.trim())
            }
        }
    }

    private fun sendResult() {
        requireActivity().supportFragmentManager.setFragmentResult(
            TAG,
            bundleOf(STREAM_NAME_KEY to binding.editTextStreamName.text.toString().trim())
        )
    }

    private fun showToastBlankStreamName() {
        Toast.makeText(
            requireContext(),
            getString(R.string.stream_name_cannot_be_blank),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val STREAM_NAME_KEY = "NameKey"
        const val TAG = "CreateStreamDialogFragmentTag"
    }
}