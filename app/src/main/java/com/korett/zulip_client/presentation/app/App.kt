package com.korett.zulip_client.presentation.app

import android.app.Application
import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.korett.zulip_client.di.component.AppComponent
import com.korett.zulip_client.di.component.DaggerAppComponent

class App : Application() {

    private val cicerone = Cicerone.create()
    val router get() = cicerone.router
    val navigatorHolder get() = cicerone.getNavigatorHolder()

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> appComponent
        else -> (this.applicationContext as App).appComponent
    }
