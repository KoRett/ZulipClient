package com.korett.zulip_client.core.ui.utils

sealed interface LceState<out T> {
    data object Loading : LceState<Nothing>
    data class Error(val error: Throwable) : LceState<Nothing>
    data class Content<T>(val data: T) : LceState<T>
}