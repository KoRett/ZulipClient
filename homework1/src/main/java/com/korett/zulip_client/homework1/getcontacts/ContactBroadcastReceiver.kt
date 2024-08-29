package com.korett.zulip_client.homework1.getcontacts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.korett.zulip_client.homework1.MainActivity

class ContactBroadcastReceiver(private val onReceive: (data: Array<String>?) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == GetContactsActivity.GET_CONTACTS_ACTION) {
            onReceive(intent.getStringArrayExtra(MainActivity.EXTRA_CONTACTS))
        }
    }
}