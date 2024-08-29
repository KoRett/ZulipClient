package com.korett.zulip_client.presentation.tabs.streams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn

class StreamsSharedViewModel : ViewModel() {

    val searchQuery = MutableStateFlow("")

    init {
        searchQuery.launchIn(viewModelScope)
    }

}