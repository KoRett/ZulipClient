package com.korett.zulip_client.core.ui.utils

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class BaseFragment<Effect : Any, State : Any, Event : Any>(@LayoutRes layoutId: Int) :
    Fragment(layoutId), ElmRendererDelegate<Effect, State> {

    abstract val store: Store<Event, Effect, State>
}