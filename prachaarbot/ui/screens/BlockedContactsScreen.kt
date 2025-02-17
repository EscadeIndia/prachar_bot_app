package com.prachaarbot.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prachaarbot.R
import com.prachaarbot.ui.NavScreen
import com.prachaarbot.ui.screens.viewmodel.BlockedContactsViewModel
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat
import kotlinx.coroutines.launch

@Composable
internal fun BlockedContactScreen(
    navController: NavController,
    blockedContactsViewModel: BlockedContactsViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    var contactNumber by remember { mutableStateOf("") }

    val contacts by blockedContactsViewModel.contactsListState.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        blockedContactsViewModel.fetchContactList()
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(), color = Color.White
    ) {
        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, content = {
            it.toString()
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
                        text = "Blocked contacts",
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
                        .height(25.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.width(230.dp),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.clearFocus()
                                val filteredNumber = contactNumber.replace(" ", "")
                                if ((filteredNumber.startsWith("+91") && filteredNumber.length == 13) || (!filteredNumber.startsWith(
                                        "+91"
                                    ) && filteredNumber.length == 10)
                                ) {
                                    blockedContactsViewModel.addContactToBlockedList(filteredNumber)
                                    contactNumber = ""
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Enter valid Number")
                                    }

                                }
                            }
                        ),
                        value = contactNumber,
                        onValueChange = {
                            val filteredNumber = it.replace(" ", "")
                            if ((filteredNumber.startsWith("+91") && filteredNumber.length <= 13) || (!it.startsWith(
                                    "+91"
                                ) && filteredNumber.length <= 10)
                            ) {
                                contactNumber = filteredNumber
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Search number to block",
                                fontFamily = Monstserrat,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = Grey
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp),
                        onClick = {
                            navController.navigate(NavScreen.RecentCallsScreen.route)
                        }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_phone_logs_16), "",
                            tint = Black
                        )

                    }
                    Text(
                        text = "RECENT CALLS",
                        fontFamily = Monstserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Black,
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .clickable {
                                navController.navigate(NavScreen.RecentCallsScreen.route)
                            }
                    )

                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                )

                LazyColumn {
                    items(contacts.size) {
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
                                    text = contacts[it].contactName,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Black,
                                    fontFamily = Monstserrat,

                                    )
                                Text(
                                    text = contacts[it].contactNumber,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Grey,
                                    fontFamily = Monstserrat,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                            }
                            Icon(
                                tint = Grey,
                                imageVector = Icons.Filled.Close,
                                modifier = Modifier
                                    .size(25.dp)
                                    .clickable {
                                        blockedContactsViewModel.removeContactFromBlockedList(
                                            contacts[it]
                                        )
                                    },
                                contentDescription = "",
                            )

                        }
                    }
                }
            }
        })
    }
}

