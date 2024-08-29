package com.korett.zulip_client.homework1

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.korett.zulip_client.homework1.databinding.ActivityMainBinding
import com.korett.zulip_client.homework1.getcontacts.GetContactsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val getContactsActivityResult =
        registerForActivityResult(StartActivityForResult()) { result ->
            val contactList = result.data?.getStringArrayExtra(EXTRA_CONTACTS)?.toList()
            if (result.resultCode == Activity.RESULT_OK && contactList != null) {
                binding.contactList.adapter = ContactListAdapter(contactList)
                binding.btGetContacts.visibility = View.GONE
                binding.contactList.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, R.string.contacts_not_found, Toast.LENGTH_SHORT).show()
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                getContacts()
            } else {
                Toast.makeText(this, R.string.access_to_contacts_denied, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        binding.btGetContacts.setOnClickListener {
            getContacts()
        }
    }

    private fun getContacts() {
        when {
            checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED -> {
                val intent = Intent(this, GetContactsActivity::class.java)
                getContactsActivityResult.launch(intent)
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                Toast.makeText(this, R.string.access_to_contacts_required, Toast.LENGTH_SHORT)
                    .show()
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }

    companion object {
        const val EXTRA_CONTACTS = "EXTRA_CONTACTS"
    }
}