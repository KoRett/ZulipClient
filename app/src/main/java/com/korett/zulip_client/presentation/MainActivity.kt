package com.korett.zulip_client.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.korett.zulip_client.R
import com.korett.zulip_client.core.common.extension.lazyUnsafe
import com.korett.zulip_client.databinding.ActivityMainBinding
import com.korett.zulip_client.presentation.app.App
import com.korett.zulip_client.presentation.navigation.Screens

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navigator by lazyUnsafe { AppNavigator(this, R.id.mainFragmentContainer) }
    private val navigatorHolder get() = App.INSTANCE.navigatorHolder
    private val router get() = App.INSTANCE.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.Tab())
        }

        setContentView(binding.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }
}