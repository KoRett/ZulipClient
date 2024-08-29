package com.korett.zulip_client.core.ui.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.korett.zulip_client.R

data class UserModelUi(
    val id: Int,
    val username: String,
    val email: String,
    val avatarUrl: String?,
    val status: UserStatusUi
)

sealed class UserStatusUi(@StringRes val text: Int, @ColorRes val color: Int) {
    data object Active : UserStatusUi(R.string.active, R.color.active_status_color)
    data object Idle : UserStatusUi(R.string.idle, R.color.idle_status_color)
    data object Offline : UserStatusUi(R.string.offline, R.color.offline_status_color)
    data object Unknown : UserStatusUi(R.string.unknown, R.color.transparent)
}