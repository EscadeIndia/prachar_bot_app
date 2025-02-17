package com.prachaarbot.ui.screens.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.provider.CallLog
import androidx.lifecycle.ViewModel
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.local.sharedPreference.model.ContactData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
internal class RecentCallsViewModel
@Inject constructor(
    private val localSharedPref: LocalPrefData,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _contactsListState =
        MutableStateFlow<MutableList<ContactData>>(mutableListOf())
    val contactsListState = _contactsListState.asStateFlow()

    init {
        _contactsListState.value = getCallDetails(context)
    }

    fun getCallDetails(context: Context): MutableList<ContactData> {
        val callLogData = mutableListOf<ContactData>()
        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val nameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)

            while (it.moveToNext()) {
                val number = it.getString(numberIndex)
                val name = it.getString(nameIndex)
                val contactData = ContactData(name ?: "Unknown", number.replace(" ", ""))
                if(contactData !in callLogData)
                    callLogData.add(contactData)
            }
        }
        val updatedList = callLogData.filterNot { contact ->
            (localSharedPref.blockedContacts
                ?: mutableListOf()).any { it.contactNumber == contact.contactNumber }
        }
        return updatedList.toMutableList()
    }

    fun addContactToBlockedList(contactData: ContactData) {
        val filteredList = mutableListOf<ContactData>()
        filteredList.addAll(_contactsListState.value)
        filteredList.remove(contactData)
        _contactsListState.value = filteredList
        contactData.contactNumber = if (contactData.contactNumber.startsWith("+91").not()) {
            "+91${contactData.contactNumber}"
        } else contactData.contactNumber
        val contactsList = localSharedPref.blockedContacts?.toMutableList() ?: mutableListOf()
        if (contactsList.contains(contactData).not()) {
            contactsList.add(contactData)
            localSharedPref.blockedContacts = contactsList

        }
    }
}