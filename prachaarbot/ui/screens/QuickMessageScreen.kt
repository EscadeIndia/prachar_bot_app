package com.prachaarbot.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prachaarbot.R
import com.prachaarbot.ui.component.FullScreenProgress
import com.prachaarbot.ui.screens.viewmodel.QuickMessageViewModel
import com.prachaarbot.ui.state.ViewState
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@SuppressLint("Range")
@ExperimentalMaterialApi
@Composable
internal fun QuickMessageScreen(
    quickMessageViewModel: QuickMessageViewModel = hiltViewModel(), navController: NavController
) {

    val context = LocalContext.current

    var phoneNumber by remember { mutableStateOf("") }

    var messagingModeSelected by remember { mutableStateOf(0) }
    var messageSenderSelected by remember {
        mutableStateOf("WhatsApp")
    }
    val messageSenderOptionsVariant0 = mutableListOf("WhatsApp", "WhatsApp Business")
    val messageSenderOptionsVariant1 = quickMessageViewModel.fetchSimOperators()

    var showProgress by remember {
        mutableStateOf(false)
    }


    var textMessageBody by remember { mutableStateOf("") }
    var whatsAppMessageBody by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    val showSnackbar = { text: String ->
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }

    LaunchedEffect(key1 = "message body fetched", block = {
        quickMessageViewModel.messageBodyState.collectLatest {
            when (it) {
                is ViewState.Error -> {
                    showProgress = false
                    showSnackbar.invoke(it.message)
                    textMessageBody = "No message body found"
                    whatsAppMessageBody = "No message body found"
                }

                is ViewState.Initial -> {
                    showProgress = true
                }

                is ViewState.Loading -> {
                    showProgress = true
                }

                is ViewState.Success -> {

                    showProgress = false

                    it.data.data?.textMsg?.let {
                        textMessageBody = it
                    }
                    it.data.data?.whatsMsg?.let {
                        whatsAppMessageBody = it
                    }
                }
            }
        }
    })


    LaunchedEffect(key1 = null) {
        quickMessageViewModel.messageSentState.collectLatest {
            when(it) {
                is ViewState.Error -> {
                    showSnackbar.invoke(it.message)
                }
                is ViewState.Initial -> {}
                is ViewState.Loading -> {}
                is ViewState.Success -> {
                    showSnackbar.invoke(it.data)
                }
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = Color.White
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            content = {
                it.toString()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 2.dp),
                            onClick = {
                                navController.popBackStack()
                            }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack, contentDescription = ""
                            )
                        }
                        Text(
                            text = "Quick Messages",
                            fontFamily = Monstserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Black,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp)

                        )
                    }

                    Text(
                        text = "Choose a messaging mode",
                        fontFamily = Monstserrat,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Grey,
                        modifier = Modifier
                            .padding(top = 42.dp)
                            .align(alignment = Alignment.Start)
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 16.dp)
                    ) {
                        Button(
                            onClick = {
                                messagingModeSelected = 0
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(backgroundColor = if (messagingModeSelected == 0) Black else Color.White),
                            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                        ) {
                            Image(
                                colorFilter = ColorFilter.tint(if (messagingModeSelected == 0) Color.White else Black),
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(16.dp),
                                painter = painterResource(id = R.drawable.ic_whatsapp),
                                contentDescription = ""
                            )
                            Text(
                                text = "WhatsApp",
                                fontFamily = Monstserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = if (messagingModeSelected == 0) Color.White else Black,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                        Button(
                            onClick = {
                                messagingModeSelected = 1
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(backgroundColor = if (messagingModeSelected == 1) Black else Color.White),
                            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                        ) {
                            Image(
                                colorFilter = ColorFilter.tint(if (messagingModeSelected == 1) Color.White else Black),
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(16.dp),
                                painter = painterResource(id = R.drawable.ic_sms_16),
                                contentDescription = ""
                            )
                            Text(
                                text = "SMS",
                                fontFamily = Monstserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = if (messagingModeSelected == 1) Color.White else Black,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }

                    Text(
                        text = if (messagingModeSelected == 0)
                            "Choose a version of your WhatsApp to send message"
                        else
                            "Choose a SIM to send messages",
                        fontFamily = Monstserrat,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Grey,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(alignment = Alignment.Start)
                    )


                    val list = if (messagingModeSelected == 0) {
                        messageSenderOptionsVariant0
                    } else {
                        messageSenderOptionsVariant1
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                    ) {
                        list.forEach {

                            val buttonCLicked: () -> Unit = {
                                messageSenderSelected = it
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(end = 30.dp)
                                    .clickable {
                                        buttonCLicked.invoke()
                                    }
                            ) {
                                RadioButton(
                                    modifier = Modifier.size(16.dp),
                                    selected = (it == messageSenderSelected),
                                    onClick = {
                                        buttonCLicked.invoke()
                                    }
                                )
                                Text(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = if (messageSenderSelected == it) Black else Grey,
                                    fontFamily = Monstserrat,
                                    text = it,
                                    modifier = Modifier.padding(start = 12.dp)

                                )
                            }
                        }

                    }

                    OutlinedTextField(
                        enabled = false,
                        readOnly = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            disabledTextColor = Black,
                            disabledTrailingIconColor = Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        value = if (messagingModeSelected == 0) whatsAppMessageBody else textMessageBody,
                        onValueChange = {},
                        label = {
                            Text(
                                "Message Body",
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                fontFamily = Monstserrat,
                            )
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                            .heightIn(max = 300.dp)
                            .verticalScroll(rememberScrollState())

                    )
//            DropdownTextFieldWithDescription(
//                if (messagingModeSelected == 1) QuickMessageViewModel.textMessageOptions.keys.toList() else listOf(
//                    "Text Message",
//                    "Option 2",
//                    "Option 3"
//                )
//            ) {
//                selectedMessageBody = it
//            }

//            if(messagingModeSelected == 0) {
//                Row(
//                    modifier = Modifier
//                        .align(Alignment.End)
//                        .padding(top = 8.dp)
//                ) {
//                    Text(
//                        text = "Change your message body",
//                        fontFamily = Monstserrat,
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 12.sp,
//                        color = Black,
//
//                        )
//                    Text(
//                        textDecoration = TextDecoration.Underline,
//                        text = "here",
//                        fontFamily = Monstserrat,
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 12.sp,
//                        color = MaterialTheme.colors.primary,
//                        modifier = Modifier
//                            .padding(start = 2.dp)
//                            .clickable {
//
//                            },
//                    )
//                }
//            }


                    val focusManager = LocalFocusManager.current
                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.PickContact()
                    ) { contactUri ->
                        contactUri?.let { uri ->
                            val cursor = context.contentResolver.query(uri, null, null, null, null)
                            cursor?.use {
                                if (it.moveToFirst()) {
                                    val id =
                                        it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                                    val hasPhone =
                                        it.getInt(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                                    if (hasPhone > 0) {
                                        val phonesCursor = context.contentResolver.query(
                                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                            null,
                                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                            arrayOf(id),
                                            null
                                        )
                                        phonesCursor?.use { pCursor ->
                                            if (pCursor.moveToFirst()) {
                                                phoneNumber = pCursor.getString(
                                                    pCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            cursor?.close()
                        }
                    }


                    val requestContactPermission = rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestPermission()
                    ) { isGranted ->
                        if (isGranted) {
                            launcher.launch()
                        } else {

                        }
                    }

                    OutlinedTextField(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                        value = phoneNumber,
                        onValueChange = { newNumber ->
                            phoneNumber = newNumber.filter { it.isDigit() }
                        },
                        placeholder = {
                            Text(
                                text = "Enter Mobile Number",
                                fontFamily = Monstserrat,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = Grey
                            )
                        },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        if (ContextCompat.checkSelfPermission(
                                                context, Manifest.permission.READ_CONTACTS
                                            ) == PackageManager.PERMISSION_GRANTED
                                        ) {
                                            launcher.launch()
                                        } else {
                                            requestContactPermission.launch(Manifest.permission.READ_CONTACTS)
                                        }
                                    },
                                tint = Black,
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_contact_16),
                                contentDescription = ""
                            )

                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            if (phoneNumber.isEmpty()) {
                                showSnackbar.invoke("Enter Phone Number")
                            } else if ((messagingModeSelected == 0 && !messageSenderOptionsVariant0.contains(
                                    messageSenderSelected
                                )) || (messagingModeSelected == 1 && !messageSenderOptionsVariant1.contains(
                                    messageSenderSelected
                                ))

                            ) {
                                showSnackbar.invoke("Select the provider to send message")

                            } else {
                                quickMessageViewModel.senMessageToNumber(
                                    messagingModeSelected,
                                    messageSenderSelected,
                                    if (messagingModeSelected == 0) whatsAppMessageBody else textMessageBody,
                                    phoneNumber,
                                ) {
                                    context.startActivity(it)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .height(53.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Send Message",
                            fontFamily = Monstserrat,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            })
    }

    if (showProgress) {
        FullScreenProgress()
    }
}