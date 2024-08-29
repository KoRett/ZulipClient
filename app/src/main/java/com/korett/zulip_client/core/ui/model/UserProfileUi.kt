package com.korett.zulip_client.core.ui.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.korett.zulip_client.R

data class UserProfileUi(
    val id: Int,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val status: UserProfileStatusUi
)

sealed class UserProfileStatusUi(@StringRes val text: Int, @ColorRes val color: Int) {
    data object Active : UserProfileStatusUi(R.string.active, R.color.active_status_color)
    data object Idle : UserProfileStatusUi(R.string.idle, R.color.idle_status_color)
    data object Offline : UserProfileStatusUi(R.string.offline, R.color.offline_status_color)
}