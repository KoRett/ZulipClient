package com.korett.zulip_client.homework1.getcontacts

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.korett.zulip_client.homework1.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GetContactService : Service() {

    private var job: Job? = null
    private var scope : CoroutineScope? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        job = Job()
        scope = CoroutineScope(Dispatchers.IO + job!!)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope?.launch {
            val contacts = getContacts()
            LocalBroadcastManager.getInstance(this@GetContactService).sendBroadcast(
                Intent(GetContactsActivity.GET_CONTACTS_ACTION)
                    .putExtra(MainActivity.EXTRA_CONTACTS, contacts)
            )
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun getContacts(): Array<String>? {
        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        var contacts: ArrayList<String>? = null
        cursor?.use {
            with(it) {
                if (cursor.count > 0) {
                    contacts = arrayListOf()
                }
                while (moveToNext()) {
                    val nameIndex = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                    contacts!!.add(getString(nameIndex))
                }
            }
        }
        return contacts?.toTypedArray()
    }

}