package com.korett.zulip_client.homework1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korett.zulip_client.homework1.databinding.ContactListItemBinding

class ContactListAdapter(private val data: List<String>) :
    ListAdapter<String, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ContactListViewHolder).bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactListViewHolder.create(parent)
    }

    override fun getItemCount(): Int = data.size

    private class ContactListViewHolder(private val binding: ContactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            binding.textContactName.text = item
        }

        companion object {
            fun create(view: ViewGroup): ContactListViewHolder {
                val inflater = LayoutInflater.from(view.context)
                val binding = ContactListItemBinding.inflate(inflater, view, false)
                return ContactListViewHolder(binding)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}