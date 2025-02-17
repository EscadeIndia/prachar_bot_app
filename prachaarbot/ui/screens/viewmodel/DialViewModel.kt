package com.prachaarbot.ui.screens.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import kotlinx.coroutines.launch
class DialViewModel(application: Application) : AndroidViewModel(application) {
    private val _contactName = MutableLiveData<String>()
    val contactName: LiveData<String> get() = _contactName

    // Function to check contact name based on the phone number
    fun checkContactName(phoneNumber: String) {
        if (phoneNumber.isNotEmpty() && phoneNumber != "+91") {
            var normalizedNumber = phoneNumber.replace(Regex("[^0-9]"), "") // Clean up the number

            // Handle potential country code mismatch
            if (normalizedNumber.length < 10) {  // If it's a local number
                normalizedNumber = "+91$normalizedNumber"  // Append country code
            }

            if (normalizedNumber.length > 3) {
                // Query contacts here in a background thread
                viewModelScope.launch {
                    val contactName = getContactNameFromPhoneNumber(normalizedNumber, getApplication<Application>().applicationContext)
                    _contactName.postValue(contactName)
                }
            }
        }
    }

    @SuppressLint("Range")
    private suspend fun getContactNameFromPhoneNumber(phoneNumber: String, context: Context): String {
        var contactName = "Unknown" // Default name if not found
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
            ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?",
            arrayOf("%$phoneNumber%"),
            null
        )

        cursor?.use {
            // Check if the cursor has a result
            if (it.moveToFirst()) {
                // Fetch the contact name
                contactName = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) ?: "Unknown"
            }
        }

        return contactName
    }
}
