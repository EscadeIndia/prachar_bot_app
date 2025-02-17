package com.prachaarbot.ui.screens

import android.content.Context
import android.provider.CallLog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.core.content.ContextCompat
import com.prachaarbot.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ViewAllScreen(navController: NavHostController) {
    val context = LocalContext.current
    val callDetails = remember { mutableStateListOf<CallDetail>() }

    // Fetch the call logs
    LaunchedEffect(Unit) {
        callDetails.clear()
        callDetails.addAll(fetchCallLogs(context))
    }

    // Display the UI components
    Column(modifier = Modifier.fillMaxSize()) {
        // Title Section with Back Button and Text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(onClick = { /* Handle back button click */ }) {
                Icon(painter = painterResource(id = R.drawable.back), contentDescription = "Back")
            }
            Text(
                text = "Recent calls",
                style = MaterialTheme.typography.h6.copy(color = Color.Black, fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        // List of Calls using LazyColumn
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(callDetails) { call ->
                CallItem(call)
                Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
            }
        }
    }
}

@Composable
fun CallItem(callDetail: CallDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Call Details Left Section (Phone number and time)
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {

            // Box to contain the image and background color, with width 80dp
            Box(
                modifier = Modifier
                    .width(60.dp) // Set width of the box to 80dp
                    .height(70.dp) // Set height of the box to 80dp
                    .padding(10.dp) // Padding around the box
                    .background(
                        color = Color(0xFFFDE392), // Background color for the box (yellow2)
                        shape = RoundedCornerShape(80.dp) // Rounded corners for the box
                    )
            ) {
                // Main Image placed inside the Box, with width 60dp
                Image(
                    painter = painterResource(id = R.drawable.watermark_logo), // Your watermark image
                    contentDescription = "Watermark",
                    modifier = Modifier
                        .width(40.dp) // Set width of the image to 60dp
                        .height(40.dp)
                        .padding(7.dp)// Set height of the image to 60dp
                        .align(Alignment.Center) // Center the image inside the box
                )
            }

            // Column for the phone number and time details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = callDetail.number,
                    style = MaterialTheme.typography.body1,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 14.dp)
                )
                Text(
                    text = callDetail.time,
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }

        // Call Action Buttons (WhatsApp, Message, Call)
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Handle WhatsApp click */ }) {
                Icon(
                    painter = painterResource(id = callDetail.whatsappIcon),
                    contentDescription = "WhatsApp",
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = { /* Handle Message click */ }) {
                Icon(
                    painter = painterResource(id = callDetail.messageIcon),
                    contentDescription = "Message",
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = { /* Handle Call click */ }) {
                Icon(
                    painter = painterResource(id = callDetail.callIcon),
                    contentDescription = "Call",
                    modifier = Modifier
                        .size(30.dp)
                        .padding(7.dp)
                        .apply {
                            when (callDetail.callType) {
                                CallType.Incoming -> {
                                    this.background(Color.Green)
                                }
                                CallType.Outgoing -> {
                                    this.background(Color.Blue)
                                }
                                CallType.Missed -> {
                                    this.background(Color.Red)
                                }
                            }
                        }
                )
            }
        }
    }
}

// Define the CallDetail data model
data class CallDetail(
    val number: String,
    val time: String,
    val callType: CallType,
    val whatsappIcon: Int,
    val messageIcon: Int,
    val callIcon: Int
)

enum class CallType {
    Incoming,
    Outgoing,
    Missed
}

// Helper function to fetch call logs
fun fetchCallLogs(context: Context): List<CallDetail> {
    val callList = mutableListOf<CallDetail>()
    val contentResolver = context.contentResolver
    val todayStartTimestamp = getStartOfDayTimestamp()

    val cursor = contentResolver.query(
        CallLog.Calls.CONTENT_URI,
        arrayOf(CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.TYPE),
        null,
        null,
        "${CallLog.Calls.DATE} DESC"
    )

    cursor?.use {
        val numberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
        val typeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
        val dateIndex = it.getColumnIndex(CallLog.Calls.DATE)

        while (it.moveToNext()) {
            val phoneNumber = it.getString(numberIndex)
            val callType = it.getInt(typeIndex)
            val callDate = it.getLong(dateIndex)

            if (callDate >= todayStartTimestamp) {
                val callTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(callDate))

                val callDetail = when (callType) {
                    CallLog.Calls.INCOMING_TYPE -> CallDetail(
                        number = phoneNumber,
                        time = callTime,
                        callType = CallType.Incoming,
                        whatsappIcon = R.drawable.ic_whatsapp,
                        messageIcon = R.drawable.message_on_recent,
                        callIcon = R.drawable.call
                    )
                    CallLog.Calls.OUTGOING_TYPE -> CallDetail(
                        number = phoneNumber,
                        time = callTime,
                        callType = CallType.Outgoing,
                        whatsappIcon = R.drawable.ic_whatsapp,
                        messageIcon = R.drawable.message_on_recent,
                        callIcon = R.drawable.call
                    )
                    CallLog.Calls.MISSED_TYPE -> CallDetail(
                        number = phoneNumber,
                        time = callTime,
                        callType = CallType.Missed,
                        whatsappIcon = R.drawable.ic_whatsapp,
                        messageIcon = R.drawable.message_on_recent,
                        callIcon = R.drawable.call
                    )
                    else -> null
                }
                callDetail?.let { callList.add(it) }
            }
        }
    }

    return callList
}

// Helper function to get the timestamp for the start of today (midnight)
private fun getStartOfDayTimestamp(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}


