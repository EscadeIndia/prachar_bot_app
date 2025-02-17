package com.prachaarbot.ui.screens.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.Data
import com.prachaarbot.data.model.GetMessageBodyResponse
import com.prachaarbot.data.model.WhatsAppMessageBody
import com.prachaarbot.data.remote.Response
import com.prachaarbot.domain.use_case.SentMessageUseCase
import com.prachaarbot.ui.state.ViewState
import com.prachaarbot.utils.formatPhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@SuppressLint("StaticFieldLeak")
internal class QuickMessageViewModel
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val localPrefData: LocalPrefData,
    private val sentMessageUseCase: SentMessageUseCase
) : ViewModel() {


    private val _messageBodyState =
        MutableStateFlow<ViewState<GetMessageBodyResponse>>(ViewState.Initial())
    val messageBodyState = _messageBodyState.asStateFlow()


    val whatsAppMessageBodyOptions = mutableListOf<WhatsAppMessageBody>()

    private val _messageSentState =
        MutableStateFlow<ViewState<String>>(ViewState.Initial())
    val messageSentState = _messageSentState.asStateFlow()

    init {
        if (localPrefData.textMessageBody.isNullOrBlank() || localPrefData.whatsAppMessageBody.isNullOrBlank()) {
            viewModelScope.launch(Dispatchers.IO) {
                sentMessageUseCase.getSmsBody().distinctUntilChanged().collect {
                    when (it) {
                        is Response.Error -> _messageBodyState.value = ViewState.Error(it.message)
                        is Response.Success -> {
                            sentMessageUseCase.updatePref(it.data.data)
                            addWhatsAppMessageBodyOptions(it.data.data)
                            _messageBodyState.value = ViewState.Success(it.data)
                        }
                    }
                }
            }
        } else {
            _messageBodyState.value = ViewState.success(
                GetMessageBodyResponse(
                    data = Data(
                        textMsg = localPrefData.textMessageBody,
                        whatsMsg = localPrefData.whatsAppMessageBody
                    )
                )
            )
        }


    }

    private val subscriptionManager =
        ContextCompat.getSystemService(context, SubscriptionManager::class.java)


    fun fetchSimOperators(): List<String> {
        val simOperatorNames = mutableListOf<String>()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            subscriptionManager?.activeSubscriptionInfoList?.forEach {
                simOperatorNames.add(it.displayName.toString())
            }
        }
        if (simOperatorNames.size == 2 && simOperatorNames[0] == simOperatorNames[1]) {
            simOperatorNames[0] += " SIM 1"
            simOperatorNames[1] += " SIM 2"
        }

        return simOperatorNames
    }


    fun senMessageToNumber(
        variantSelected: Int,
        variantName: String,
        message: String,
        number: String,
        sendMessageUsingIntent: (intent: Intent) -> Unit
    ) {

        formatPhoneNumber(number)?.let {
            if (variantSelected == 0) {

                when (variantName) {
                    "WhatsApp" -> {
                        if (localPrefData.whatsAppMessageBodyImage.isNullOrBlank().not()) {
                            try {
                                Intent(Intent.ACTION_SEND).apply {
                                    setPackage("com.whatsapp")

                                    val updatedNumber =
                                        number.replace("+", "").replace(" ", "").let {
                                            if (it.startsWith("91")) it else "91$it" // Add country code if not present
                                        }

                                    // Format the URL for unsaved contacts
                                    // Attach the image if necessary
                                    putExtra(
                                        Intent.EXTRA_STREAM,
                                        localPrefData.whatsAppMessageBodyImage!!.toUri()
                                    )
                                    type = "image/*"  // The type for the image

                                    // Attach the message text (Text will be sent along with the image)
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        localPrefData.whatsAppMessageBody
                                    ) // Text message

                                    // Grant URI permission to WhatsApp
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                                    // WhatsApp allows sending text to a specific number, but it doesn't accept it in the URL along with the image
                                    val whatsappUrl = Uri.parse("https://wa.me/$updatedNumber")
                                    data = whatsappUrl
                                }.also { sendMessageUsingIntent.invoke(it) }

                            } catch (e: Exception) {
                                Intent(Intent.ACTION_SEND).apply {
                                    setPackage("com.whatsapp")
                                    putExtra(
                                        Intent.EXTRA_STREAM,
                                        localPrefData.whatsAppMessageBodyImage!!.toUri()
                                    )
                                    type = "image/*"
                                    val updatedNumber =
                                        number.replace("+", "").replace(" ", "").let {
                                            if (it.startsWith("91")) it else "91$it"
                                        }
                                    putExtra("jid", "$updatedNumber@s.whatsapp.net")
                                    putExtra(Intent.EXTRA_TEXT, message)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                                }.also { sendMessageUsingIntent.invoke(it) }
                            }

                        } else {
                            Intent(Intent.ACTION_VIEW).apply {
                                data =
                                    Uri.parse(
                                        "https://api.whatsapp.com/send?phone=$it&text=${
                                            Uri.encode(
                                                message
                                            )
                                        }"
                                    )
                                setPackage("com.whatsapp")
                            }.also { sendMessageUsingIntent.invoke(it) }
                        }
                    }

                    "WhatsApp Business" -> {
                        if (localPrefData.whatsAppMessageBodyImage.isNullOrBlank().not()) {
                            Intent(Intent.ACTION_VIEW).apply {
                                setPackage("com.whatsapp.w4b")

                                val updatedNumber = number.replace("+", "").replace(" ", "").let {
                                    if (it.startsWith("91")) it else "91$it" // Add country code if not present
                                }

                                // Format the URL for unsaved contacts


                                // Attach the image if necessary
                                putExtra(
                                    Intent.EXTRA_STREAM,
                                    localPrefData.whatsAppMessageBodyImage!!.toUri()
                                )
                                type = "image/*"

                                // Grant URI permission to WhatsApp
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                val whatsappUrl = Uri.parse(
                                    "https://wa.me/$updatedNumber?text=${
                                        Uri.encode(localPrefData.whatsAppMessageBody)
                                    }"
                                )
                                data = whatsappUrl
                            }.also { sendMessageUsingIntent.invoke(it) }

//                            Intent(Intent.ACTION_SEND).apply {
//                                setPackage("com.whatsapp.w4b")
//                                putExtra(
//                                    Intent.EXTRA_STREAM,
//                                    localPrefData.whatsAppMessageBodyImage!!.toUri()
//                                )
//                                type = "image/*"
//                                val updatedNumber = it.replace("+", "")
//                                putExtra("jid", "$updatedNumber@s.whatsapp.net")
//                                putExtra(Intent.EXTRA_TEXT, message)
//                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//                            }.also { sendMessageUsingIntent.invoke(it) }
                        } else {
                            Intent(Intent.ACTION_VIEW).apply {
                                // Replace with the phone number including the country code
                                data =
                                    Uri.parse(
                                        "https://api.whatsapp.com/send?phone=$it&text=${
                                            Uri.encode(
                                                message
                                            )
                                        }"
                                    )
                                setPackage("com.whatsapp.w4b")
                            }.also { sendMessageUsingIntent.invoke(it) }
                        }
                    }

                    else -> {}
                }
            } else {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_PHONE_STATE
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.SEND_SMS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val selectedSim =
                        subscriptionManager?.activeSubscriptionInfoList?.firstOrNull { it.displayName == variantName }

                    val smsManager =
                        selectedSim?.let { SmsManager.getSmsManagerForSubscriptionId(it.subscriptionId) }

                    // Send the SMS
                    if (message.length > 160) {
                        smsManager?.sendMultipartTextMessage(
                            it,
                            null,
                            smsManager.divideMessage(message),
                            null,
                            null
                        )
                    } else {
                        smsManager?.sendTextMessage(it, null, message, null, null)
                        _messageSentState.value = ViewState.Success("Send message requested")

                    }
                } else {
                    _messageSentState.value =
                        ViewState.Error("Grant read phone state and send sms permission first")
                }

            }
        } ?: run {
            _messageSentState.value =
                ViewState.Error("Phone number is invalid")
        }

    }

    fun addWhatsAppMessageBodyOptions(data: Data?) {
        if (localPrefData.user?.userName.isNullOrBlank().not()) {
            whatsAppMessageBodyOptions.add(
                WhatsAppMessageBody(
                    "Profile Url",
                    "http://chillerz.mpacaligarh.com/api/profile/${localPrefData.user?.userName}"
                )
            )
        }
        if (data?.whatsMsg.isNullOrBlank().not()) {
            whatsAppMessageBodyOptions.add(
                WhatsAppMessageBody(
                    "Text Message",
                    data?.whatsMsg!!
                )
            )
        }
        if (data?.news?.getOrNull(0)?.title.isNullOrBlank().not()) {
            whatsAppMessageBodyOptions.add(
                WhatsAppMessageBody(
                    "News",
                    data?.news?.get(0)?.title!!,
                    data.news.get(0).img
                )
            )
        }
    }

}