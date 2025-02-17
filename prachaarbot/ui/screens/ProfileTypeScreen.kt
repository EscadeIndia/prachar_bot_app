package com.prachaarbot.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prachaarbot.R
import com.prachaarbot.ui.NavScreen
import com.prachaarbot.ui.component.FullScreenProgress
import com.prachaarbot.ui.screens.viewmodel.ProfileTypeViewModel
import com.prachaarbot.ui.state.ViewState
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat
import com.prachaarbot.ui.theme.Segoe
import com.prachaarbot.utils.formatPhoneNumber
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("Range")
@ExperimentalMaterialApi
@Composable
internal fun ProfileTypeScreen(
    profileTypeViewModel: ProfileTypeViewModel = hiltViewModel(), navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

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
    LaunchedEffect(key1 = "User Profile booth", block = {
        profileTypeViewModel.userprofileBoothWorkerState.collectLatest {
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
                    navController.navigate(NavScreen.Home.route)
                }
            }
        }
    })

    LaunchedEffect(key1 = "User Profile admin", block = {
        profileTypeViewModel.userprofileAdminState.collectLatest {
            when (it) {
                true -> {
                    showProgress = false
                    navController.popBackStack()
                    navController.navigate(NavScreen.Home.route)
                }

                false -> {
                    showProgress = false
                    showSnackbar.invoke("Secret key didn't match.Try again ..")
                }

                null -> {

                }
            }
        }
    })

    Surface(
        modifier = Modifier
            .fillMaxSize(), color = White
    ) {
        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, content = {
            it.toString()
            val options = listOf("I am an agent", "I own this app")
            var selectedProfileOption by remember { mutableStateOf("") }

            Column(verticalArrangement = Arrangement.SpaceBetween) {

                Spacer(modifier = Modifier.padding(top = 20.dp))
                Image(
                    modifier = Modifier
                        .width(50.dp)
                        .height(34.dp)
                        .padding(start = 16.dp),
                    painter = painterResource(R.drawable.logo_prachaarbot),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = ""
                )

                Text(
                    text = "Get started",
                    fontFamily = Monstserrat,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Black,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(horizontal = 16.dp)
                )
                Text(
                    text = "Please choose your profile type to continue.",
                    fontFamily = Monstserrat,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Grey,
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .padding(horizontal = 16.dp)

                )


                options.forEachIndexed { index, option ->
                    val color = if (selectedProfileOption == option) Black else Grey
                    OutlinedButton(
                        border = BorderStroke(width = 1.dp, color = color),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),

                        onClick = {
                            selectedProfileOption = option
                            username = ""
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                tint = color,
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_check_circle_outline_24),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = option,
                                fontFamily = Segoe,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = color,
                                modifier = Modifier
                                    .padding(start = 6.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                AnimatedVisibility(
                    visible = selectedProfileOption.isNotBlank(),
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),

                    ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        backgroundColor = White,
                        elevation = 10.dp,
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                        ) {


                            Text(
                                text = if (selectedProfileOption == options[0]) "Enter your details to continue." else "Enter your secret text for validation",
                                fontFamily = Segoe,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                color = Black,
                                modifier = Modifier
                                    .padding(top = 30.dp)
                                    .align(alignment = Alignment.Start)
                            )

                            val focusRequester = remember { FocusRequester() }
                            val focusManager = LocalFocusManager.current
                            var error by remember {
                                mutableStateOf(false)
                            }
                            OutlinedTextField(
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        if (selectedProfileOption == options[0]) {
                                            focusRequester.requestFocus()
                                        } else {
                                            focusManager.clearFocus()

                                        }
                                    }
                                ),
                                isError = error && username.isNullOrBlank(),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp)
                                    .onKeyEvent {
                                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                                            focusRequester.requestFocus()
                                            true
                                        } else false
                                    },
                                value = username,
                                onValueChange = {
                                    username = it
                                },
                                placeholder = {
                                    Text(
                                        text = if (error && username.isNullOrBlank()) "This Field can't be blank" else if (selectedProfileOption == options[0]) "Fullname" else "Your secret text",
                                        fontFamily = Segoe,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = Grey
                                    )
                                },
                                trailingIcon = {
                                    if (error && username.isNullOrBlank()) {
                                        Icon(
                                            imageVector = Icons.Filled.Error,
                                            contentDescription = "Error Icon",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            )

                            var phoneNumberValid by remember {
                                mutableStateOf(true)
                            }
                            if (selectedProfileOption == options[0]) {
                                OutlinedTextField(
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    value = phoneNumber,
                                    onValueChange = { newNumber ->
                                        phoneNumber = newNumber
                                        phoneNumberValid =
                                            formatPhoneNumber(phoneNumber).isNullOrBlank().not()

                                    },
                                    placeholder = {
                                        Text(
                                            text = if (phoneNumberValid) "Mobile Number" else "Enter Valid Number",
                                            fontFamily = Segoe,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            color = if (phoneNumberValid) Grey else Color.Red
                                        )
                                    },
                                    isError = phoneNumberValid.not(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    trailingIcon = {
                                        if (phoneNumberValid.not()) {
                                            Icon(
                                                imageVector = Icons.Filled.Error,
                                                contentDescription = "Error Icon",
                                                tint = Color.Red
                                            )
                                        }
                                    }
                                )
                            }


                            Button(
                                onClick = {
                                    if (username.isNullOrBlank()) {
                                        error = true
                                    } else if (selectedProfileOption == options.get(0)) {
                                        error = false
                                        formatPhoneNumber(phoneNumber)?.let {
                                            profileTypeViewModel.setUserProfileToBoothWorker(
                                                username,
                                                it
                                            )
                                        } ?: run {
                                            showSnackbar.invoke("Phone number is invalid")
                                        }
                                    } else {
                                        error = false
                                        profileTypeViewModel.checkForOwnerVerification(username)
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
                                    text = "Continue",
                                    fontFamily = Segoe,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = White
                                )
                            }
                        }

                    }
                }


            }
        })

    }


    if (showProgress) {
        FullScreenProgress()
    }
}


