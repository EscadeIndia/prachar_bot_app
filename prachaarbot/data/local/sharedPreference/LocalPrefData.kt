package com.prachaarbot.data.local.sharedPreference

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.prachaarbot.data.local.sharedPreference.model.ContactData
import com.prachaarbot.data.model.User
import javax.inject.Inject


private const val s = "PrivacyDisclosureShown"

class LocalPrefData @Inject constructor(private val sharedPreference: AppSharedPreferenceProvider) {

    var user: User?
        get() = sharedPreference.getString("User", null)?.let {
            GsonBuilder().create().fromJson(it, User::class.java)
        } ?: run {
            null
        }
        set(value) = sharedPreference.put(value, "User")

    var previousNumber : String?
        get() = sharedPreference.getString("PreviousNumber", "")
        set(value) = sharedPreference.putString("PreviousNumber", value)

    var previousNumberTime : Long
        get() = sharedPreference.getLong("PreviousNumberTime", 0)
        set(value) = sharedPreference.putLong("PreviousNumberTime", value)


    var isAppActive  : Boolean
    get() = sharedPreference.getBoolean("IsAppActive", true)
    set(value) = sharedPreference.putBoolean("IsAppActive", value)

    var blockedContacts: List<ContactData>?
        get() {
            val json = sharedPreference.getString("BlockedContacts", null)
            val type = object : TypeToken<List<ContactData>>() {}.type
            return GsonBuilder().create().fromJson(json, type)
        }
        set(value) {
            val json = GsonBuilder().create().toJson(value)
            sharedPreference.putString("BlockedContacts", json)
        }
    var autoSendMessageFlag  : Boolean
    get() = sharedPreference.getBoolean("AutoSendMessageFlag", false)
    set(value) = sharedPreference.putBoolean("AutoSendMessageFlag", value)

    var isUserLoggedIn: Boolean
        get() = sharedPreference.getBoolean("UserLoginState", false)
        set(value) = sharedPreference.putBoolean("UserLoginState", value)

    var textMessageBody : String?
        get() = sharedPreference.getString("TextMessageBody", "")
        set(value) = sharedPreference.putString("TextMessageBody", value)

    var whatsAppMessageBodyImage : String?
        get() = sharedPreference.getString("WhatsAppMessageBodyImage", "")
        set(value) = sharedPreference.putString("WhatsAppMessageBodyImage", value)
    var whatsAppMessageBody : String?
        get() = sharedPreference.getString("WhatsAppMessageBody", "")
        set(value) = sharedPreference.putString("WhatsAppMessageBody", value)
    var boothId: String?
        get() = sharedPreference.getString("BoothId", null)
        set(value) = sharedPreference.putString("BoothId", value)
    var isUserOwner: Boolean
        get() = sharedPreference.getBoolean("IsUserOwner", false)
        set(value) = sharedPreference.putBoolean("IsUserOwner", value)


    var dataTypeSelected: String?
        get() = sharedPreference.getString("DataType", "Text Message")
        set(value) = sharedPreference.putString("DataType", value)

    var dataValueSelected: String?
        get() = sharedPreference.getString("DataValue", "1")
        set(value) = sharedPreference.putString("DataValue", value)

    var defaultSimSelected: Int
        get() = sharedPreference.getInt("DefaultSimSelected", 0)
        set(value) = sharedPreference.putInt("DefaultSimSelected", value)
    var messageLimitCount: Int
        get() = sharedPreference.getInt("MessageLimitCount", 2)
        set(value) = sharedPreference.putInt("MessageLimitCount", value)
    var isWhatsAppMessageEnabled: Boolean
        get() = sharedPreference.getBoolean("WhatsAppMessageEnabled", true)
        set(value) = sharedPreference.putBoolean("WhatsAppMessageEnabled", value)
    var isTextMessageEnabled: Boolean
        get() = sharedPreference.getBoolean("TextMessageEnabled", true)
        set(value) = sharedPreference.putBoolean("TextMessageEnabled", value)
    var whatsAppWaitingTime: Int
        get() = sharedPreference.getInt("WhatsAppWaitingTime", 3)
        set(value) = sharedPreference.putInt("WhatsAppWaitingTime", value)

    var whatsAppBulkWaitingTime: Int
        get() = sharedPreference.getInt("WhatsAppBulkWaitingTime", 3)
        set(value) = sharedPreference.putInt("WhatsAppBulkWaitingTime", value)

    var authToken: String
        get() = sharedPreference.getString("token", "").toString()
        set(value) = sharedPreference.putString("token", value)

    var privacyDisclosureShown: Boolean
        get() = sharedPreference.getBoolean("PrivacyDisclosureShown", false)
        set(value) = sharedPreference.putBoolean("PrivacyDisclosureShown", value)

    var accessibilityDisclosureShown: Boolean
        get() = sharedPreference.getBoolean("AccessibilityDisclosureShown", false)
        set(value) = sharedPreference.putBoolean("AccessibilityDisclosureShown", value)

    fun clearSharedPrefData() {
        sharedPreference.clearData()
    }
}