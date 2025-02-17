package com.prachaarbot.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prachaarbot.R
import com.prachaarbot.ui.NavScreen
import com.prachaarbot.ui.component.AddExtraSpaceWithDividerInBetween
import com.prachaarbot.ui.component.MessageLimitSetterFeature
import com.prachaarbot.ui.component.SimSelectorFeature
import com.prachaarbot.ui.component.SliderFeature
import com.prachaarbot.ui.component.ToggleFeature
import com.prachaarbot.ui.screens.viewmodel.SettingsViewModel
import com.prachaarbot.ui.state.ViewState
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat
import kotlinx.coroutines.launch

@Composable
internal fun SettingsScreen(

    navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var showProgress by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    val showSnackbar = { text: String ->
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }

    }
    LaunchedEffect(key1 = "Settings Screen", block = {
        settingsViewModel.userState.collect {
            when (it) {
                is ViewState.Error -> {
                    showProgress = false
                    showSnackbar.invoke(it.message)
                }

                is ViewState.Initial -> {}
                is ViewState.Loading -> {
                    showProgress = true
                }

                is ViewState.Success -> {
                    showProgress = false
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate(NavScreen.SignIn.route)
                }
            }
        }
    })

    LaunchedEffect(key1 = null, block = {
        settingsViewModel.savedDataState.collect {
            when (it) {
                is ViewState.Error -> {}

                is ViewState.Initial -> {}
                is ViewState.Loading -> {}

                is ViewState.Success -> {
                    showSnackbar.invoke(it.data)
                }
            }
        }
    })

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
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
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
                            text = "Settings",
                            fontFamily = Monstserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Black,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)

                        )
                        Button(
                            modifier = Modifier
                                .height(34.dp),
                            colors = buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
                            shape = RoundedCornerShape(4.dp),
                            onClick = {
                                settingsViewModel.saveData()
                                navController.popBackStack()
                            }) {
                            Text(
                                text = "SAVE",
                                fontFamily = Monstserrat,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Black
                            )

                        }
                    }

                    SimSelectorFeature(
                        "Set a default SIM card to send SMS.",
                        settingsViewModel.defaultSim
                    ) {
                        settingsViewModel.defaultSim = it
                    }

                    AddExtraSpaceWithDividerInBetween()

                    MessageLimitSetterFeature(
                        "Message Repetition",
                        "Set a number of SMS & WhatsApp message to send on a particular contact per day.",
                        settingsViewModel.messageLimitCount
                    ) {
                        settingsViewModel.messageLimitCount = it
                    }

                    AddExtraSpaceWithDividerInBetween()

                    ToggleFeature(
                        "Text message",
                        "Enable / Disable text message.",
                        settingsViewModel.textEnabled
                    ) {
                        settingsViewModel.textEnabled = it
                    }

                    AddExtraSpaceWithDividerInBetween()

                    ToggleFeature(
                        "WhatsApp message",
                        "Enable / Disable WhatsApp message.",
                        settingsViewModel.whatsAppEnabled
                    ) {
                        settingsViewModel.textEnabled = it
                    }

                    AddExtraSpaceWithDividerInBetween()

                    SliderFeature(
                        "WhatsApp waiting time",
                        "WhatsApp Waiting time (in second) to send message",
                        settingsViewModel.whatsAppWaitingTime
                    ) {
                        settingsViewModel.whatsAppWaitingTime = it
                    }

                    AddExtraSpaceWithDividerInBetween()

                    SliderFeature(
                        "Bulk WhatsApp waiting time",
                        "WhatsApp Waiting time (in second) to send message",
                        settingsViewModel.whatsAppBulkWaitingTime
                    ) {
                        settingsViewModel.whatsAppBulkWaitingTime = it
                    }

                    AddExtraSpaceWithDividerInBetween(25.dp)

                    Row(modifier = Modifier.clickable {
                        navController.navigate(NavScreen.Permission.route)
                    }) {
                        Text(
                            text = "Permissions",
                            fontFamily = Monstserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Black,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Icon(
                            modifier = Modifier
                                .size(14.dp)
                                .align(Alignment.CenterVertically),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_forward_12),
                            contentDescription = ""
                        )
                    }

                    AddExtraSpaceWithDividerInBetween(25.dp)

                    Row(modifier = Modifier.clickable {
                        navController.navigate(NavScreen.BlockedContactScreen.route)
                    }) {
                        Text(
                            text = "Blocked Contact",
                            fontFamily = Monstserrat,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Black,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Icon(
                            modifier = Modifier
                                .size(14.dp)
                                .align(Alignment.CenterVertically),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_forward_12),
                            contentDescription = ""
                        )
                    }

                    Text(
                        text = settingsViewModel.getBlockedContactsSize().toString(),
                        fontFamily = Monstserrat,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Grey,
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 5.dp)
                    )

                    TextButton(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp),
                        onClick = {
                            settingsViewModel.logOut()
                        }) {
                        Text(
                            text = "Sign Out",
                            fontFamily = Monstserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color(0xFF721919)
                        )
                    }
                }
            })
    }
}

