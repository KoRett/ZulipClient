package com.korett.zulip_client.homework1.getcontacts

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.korett.zulip_client.homework1.databinding.ActivityGetContactsBinding
import com.korett.zulip_client.homework1.MainActivity

class GetContactsActivity : AppCompatActivity() {

    private val receiver = ContactBroadcastReceiver { contacts ->
        val intent = Intent().putExtra(MainActivity.EXTRA_CONTACTS, contacts)
        setResult(RESULT_OK, intent)
        finish()
    }

    private lateinit var binding: ActivityGetContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetContactsBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        startService(Intent(this, GetContactService::class.java))
        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver,
            IntentFilter(GET_CONTACTS_ACTION)
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    companion object {
        const val GET_CONTACTS_ACTION = "GET_CONTACTS_ACTION"
    }

}