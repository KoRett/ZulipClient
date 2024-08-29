package com.korett.zulip_client.core.ui.extension

import android.view.View
import android.view.ViewGroup

fun ViewGroup.getLastChild(): View = getChildAt(childCount - 1)