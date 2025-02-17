package com.prachaarbot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prachaarbot.ui.screens.viewmodel.RecentCallsViewModel
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat

@Composable
internal fun RecentCallsScreen(
    navController: NavController,
    recentCallsViewModel: RecentCallsViewModel = hiltViewModel()
) {


    val callLogs by recentCallsViewModel.contactsListState.collectAsState()

    var openDialog by remember { mutableStateOf<Pair<Boolean, Int>>(Pair(false, 0)) }
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = Color.White

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp)
            )
            Row(
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
                    text = "Recent Calls",
                    fontFamily = Monstserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Black,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)

                )

            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )

            LazyColumn {
                items(callLogs.size) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)

                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = if (callLogs[it].contactName.equals(
                                        "Unknown",
                                        true
                                    )
                                ) callLogs[it].contactNumber else callLogs[it].contactName,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Black,
                                fontFamily = Monstserrat,

                                )
                            Text(
                                text = callLogs[it].contactNumber,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Grey,
                                fontFamily = Monstserrat,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                        }
                        Icon(
                            tint = Grey,
                            imageVector = Icons.Filled.Add,
                            modifier = Modifier
                                .size(25.dp)
                                .clickable {
                                    openDialog = Pair(true, it)

                                },
                            contentDescription = "",
                        )

                    }
                }
            }
        }
        if (openDialog.first) {
            AlertDialog(
                backgroundColor = Color.White,
                shape = RoundedCornerShape(8.dp),
                onDismissRequest = { openDialog = Pair(false, 0) },
                text = { Text("Are you sure you want to block this contact?") },
                confirmButton = {
                    Button(
                        modifier = Modifier.background(Color.White),
                        onClick = {
                        openDialog = Pair(false, 0)
                    }) {
                        Text("No")
                    }
                },
                dismissButton = {
                    Button(
                        modifier = Modifier.background(Color.White),
                        onClick = {
                        recentCallsViewModel.addContactToBlockedList(callLogs[openDialog.second])
                        openDialog = Pair(false, 0)
                    }) {
                        Text("Yes")
                    }
                }

            )
        }

    }

}

