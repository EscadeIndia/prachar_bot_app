package com.prachaarbot.ui.receivers

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.CallLog
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.prachaarbot.accessibilityservice.WhatsAppAccessibilityService
import com.prachaarbot.accessibilityservice.hasAccessibilityServicePermission
import com.prachaarbot.data.local.room.dao.CallDao
import com.prachaarbot.data.local.room.entity.CallEntry
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.remote.Response
import com.prachaarbot.domain.use_case.SentMessageUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

internal class CallReceiver : BroadcastReceiver() {

    @Inject
    lateinit var callDao: CallDao

    @Inject
    lateinit var localPrefData: LocalPrefData

    @Inject
    lateinit var smsUseCase: SentMessageUseCase

    @SuppressLint("UnsafeProtectedBroadcastReceiver", "Range")
    override fun onReceive(context: Context, intent: Intent) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_CALL_LOG
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context, Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED && localPrefData.isAppActive
        ) {
            Log.d("CallReceiver", "Receiver triggered")

            if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val cursor = context.contentResolver.query(
                            CallLog.Calls.CONTENT_URI,
                            null,
                            null,
                            null,
                            CallLog.Calls.DATE + " DESC"
                        )

                        cursor?.let {
                            if (it.moveToFirst()) {
                                val number = it.getString(it.getColumnIndex(CallLog.Calls.NUMBER))
                                number?.let { num ->
                                    val tempNumber = if (num.startsWith("+91").not()) {
                                        "+91$num"
                                    } else num

                                    if ((localPrefData.blockedContacts
                                            ?: listOf()).none { it.contactNumber == tempNumber }
                                    ) {
                                        val currentTime = System.currentTimeMillis()
                                        if (localPrefData.previousNumber == number && currentTime - localPrefData.previousNumberTime < 2000) {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                saveOrSendMessageToNumber(number, context)
                                            }
                                        }
                                        localPrefData.previousNumber = number
                                        localPrefData.previousNumberTime = currentTime
                                    }
                                }
                            }
                            it.close()
                        }
                    }, 2000)
                }
            }
        }
    }

    private fun isPhoneNumberSaved(phoneNumber: String, context: Context): Boolean {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )
        val cursor = context.contentResolver.query(
            uri,
            arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
            null,
            null,
            null
        )

        return cursor?.use { it.moveToFirst() } ?: false
    }

    @SuppressLint("SuspiciousIndentation")
    private suspend fun saveOrSendMessageToNumber(number: String, context: Context) {
        val callEntry = callDao.getEntry(number) ?: CallEntry(number = number)
        if (callEntry.date.equals(LocalDate.now())) {
            callEntry.messageCount++
        } else {
            callEntry.messageCount = 0
            val updatedDate = LocalDate.now()
            callEntry.date = updatedDate
            callDao.updateDate(updatedDate)
            callDao.resetCounts()
        }
        try {
            if (callEntry.messageCount < localPrefData.messageLimitCount && ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Send SMS logic
                if (localPrefData.isTextMessageEnabled && !localPrefData.textMessageBody.isNullOrBlank()) {
                    val smsManager = SmsManager.getDefault()
                    val messageBody = localPrefData.textMessageBody

                    if (messageBody?.length!! > 160) {
                        smsManager.sendMultipartTextMessage(number, null, smsManager.divideMessage(messageBody), null, null)
                    } else {
                        smsManager.sendTextMessage(number, null, messageBody, null, null)
                    }

                    callDao.insertOrUpdate(callEntry)
                    smsUseCase.sendMessageStatus("1", number, "1", messageBody).collectLatest {
                        when (it) {
                            is Response.Error -> {
                                // Handle error
                            }

                            is Response.Success -> {
                                // Handle success
                            }
                        }
                    }
                }

                // Send WhatsApp message logic
                if (callEntry.messageCount <= localPrefData.messageLimitCount && localPrefData.isWhatsAppMessageEnabled && !localPrefData.whatsAppMessageBody.isNullOrBlank()) {
                    localPrefData.autoSendMessageFlag = true
                    val packageManager: PackageManager = context.packageManager
                    val packageName = try {
                        packageManager.getPackageInfo("com.whatsapp", 0)
                        "com.whatsapp" // WhatsApp is installed
                    } catch (e: PackageManager.NameNotFoundException) {
                        try {
                            packageManager.getPackageInfo("com.whatsapp.w4b", 0)
                            "com.whatsapp.w4b" // WhatsApp Business is installed
                        } catch (ex: PackageManager.NameNotFoundException) {
                            "" // Neither is installed
                        }
                    }

                    val updatedNumber = number.replace("+", "").replace(" ", "").let {
                        if (it.startsWith("91")) it else "91$it" // Add country code if not present
                    }

                    if (isPhoneNumberSaved(number, context)) {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            if (packageName.isNotBlank()) {
                                setPackage(packageName)
                            }
                            if (!localPrefData.whatsAppMessageBodyImage.isNullOrBlank()) {
                                putExtra(
                                    Intent.EXTRA_STREAM,
                                    Uri.parse(localPrefData.whatsAppMessageBodyImage)
                                )
                            }
                            type = "image/*"
                            putExtra("jid", "$updatedNumber@s.whatsapp.net")
                            putExtra(Intent.EXTRA_TEXT, localPrefData.whatsAppMessageBody)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            if (packageName.isNotBlank()) {
                                setPackage(packageName)
                            }
                            type = "text/plain"
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                            val whatsappUrl = Uri.parse(
                                "https://wa.me/$updatedNumber?text=${Uri.encode(localPrefData.whatsAppMessageBody)}"
                            )
                            data = whatsappUrl
                        }
                        context.startActivity(intent)
                    }

                    smsUseCase.sendMessageStatus("2", number, "1", localPrefData.whatsAppMessageBody!!).collectLatest {
                        when (it) {
                            is Response.Error -> {
                                // Handle error
                            }

                            is Response.Success -> {
                                // Handle success
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
