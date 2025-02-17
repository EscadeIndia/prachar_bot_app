package com.prachaarbot.ui.screens.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.local.sharedPreference.model.ContactData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
internal class BlockedContactsViewModel
@Inject constructor(
    private val localSharedPref: LocalPrefData,
    ) : ViewModel() {

    private val _contactsListState =
        MutableStateFlow<List<ContactData>>(listOf())
    val contactsListState = _contactsListState.asStateFlow()


    fun fetchContactList() {
        _contactsListState.value = localSharedPref.blockedContacts?: listOf()

    }
    fun removeContactFromBlockedList(contact: ContactData) {
        val contactsList = localSharedPref.blockedContacts
        localSharedPref.blockedContacts =
            contactsList?.filter { it.contactNumber != contact.contactNumber }
        _contactsListState.value = localSharedPref.blockedContacts?: listOf()
    }

    fun addContactToBlockedList(number: String) {
        val updatedNumber = if (number.startsWith("+91").not()) {
            "+91$number"
        } else number

        val contactsList = localSharedPref.blockedContacts?.toMutableList()?: mutableListOf()
        if(contactsList.contains(ContactData("UNKNOWN", updatedNumber)).not()) {
            contactsList.add(ContactData("UNKNOWN", updatedNumber))
            localSharedPref.blockedContacts = contactsList
            _contactsListState.value = localSharedPref.blockedContacts ?: listOf()
        }


    }

}