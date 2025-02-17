package com.prachaarbot.ui.screens

import android.app.Activity
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prachaarbot.R
import com.prachaarbot.ui.NavScreen
import com.prachaarbot.ui.component.FullScreenProgress
import com.prachaarbot.ui.screens.viewmodel.LoginViewModel
import com.prachaarbot.ui.state.ViewState
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Segoe
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
internal fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(), navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var showProgress by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var privacyPolicyShown by remember { mutableStateOf(loginViewModel.getPrivacyDisclosureShown()) }

    val showSnackbar = { text: String ->
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }
    LaunchedEffect(key1 = "Login Verify", block = {
        loginViewModel.userState.collectLatest {
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
                    if (loginViewModel.usersProfileTypeIsSet()) {
                        navController.navigate(NavScreen.Home.route)
                    } else {
                        navController.navigate(NavScreen.ProfileType.route)
                    }
                }
            }
        }
    })


    Surface(modifier = Modifier
        .fillMaxSize()
        .imePadding(), color = MaterialTheme.colors.primary) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            backgroundColor = MaterialTheme.colors.primary,
            content = {
                it.toString()
                Column {
                    Box(modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(), content = {
                        Image(
                            painter = painterResource(R.drawable.logo_prachaarbot),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(163.dp)
                                .width(184.dp)
                                .align(Alignment.Center)
                        )
                    })

                    Card(
                        backgroundColor = White,
                        elevation = 30.dp,
                        shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
                    ) {
                        ConstraintLayout {
                            val (headingTxt, subHeadingTxt, usernameTxt, passwordTxt, signInBtn) = createRefs()
                            Text(text = "Sign In To",
                                fontFamily = Segoe,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = Black,
                                modifier = Modifier
                                    .constrainAs(headingTxt) {
                                        absoluteLeft.linkTo(parent.absoluteLeft)
                                        absoluteRight.linkTo(parent.absoluteRight)
                                    }
                                    .padding(top = 32.dp))

                            Text(text = "PrachaarBot",
                                fontFamily = Segoe,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Black,
                                modifier = Modifier.constrainAs(subHeadingTxt) {
                                    top.linkTo(headingTxt.bottom)
                                    absoluteLeft.linkTo(parent.absoluteLeft)
                                    absoluteRight.linkTo(parent.absoluteRight)
                                })

                            val focusRequester = remember { FocusRequester() }
                            val focusManager = LocalFocusManager.current
                            OutlinedTextField(
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusRequester.requestFocus() }
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 30.dp)
                                    .constrainAs(usernameTxt) {
                                        top.linkTo(subHeadingTxt.bottom)
                                    }
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
                                        text = "Username",
                                        fontFamily = Segoe,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = Grey
                                    )
                                },
                            )

                            OutlinedTextField(
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .focusRequester(focusRequester)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 16.dp)

                                    .constrainAs(passwordTxt) {
                                        top.linkTo(usernameTxt.bottom)
                                    },
                                value = passwordText,
                                onValueChange = {
                                    passwordText = it
                                },
                                placeholder = {
                                    Text(
                                        text = "Password",
                                        fontFamily = Segoe,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                        color = Grey
                                    )
                                },
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    val image = if (passwordVisibility) Icons.Filled.Visibility
                                    else Icons.Filled.VisibilityOff
                                    IconButton(onClick = {
                                        passwordVisibility = !passwordVisibility
                                    }) {
                                        Icon(imageVector = image, "")
                                    }
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.clearFocus()
                                    }
                                ),
                            )

                            Button(
                                onClick = {
                                    loginViewModel.loginUser(username, passwordText)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 24.dp)
                                    .height(53.dp)
                                    .constrainAs(signInBtn) {
                                        top.linkTo(passwordTxt.bottom)
                                    },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Black),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Sign In",
                                    fontFamily = Segoe,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = White
                                )
                            }
                        }
                    }
                }

                if(privacyPolicyShown.not()) {
                    PrivacyPolicyDialog { result ->
                        if(result) {
                            loginViewModel.updatePrivacyDisclosureShown()
                            privacyPolicyShown = true
                        } else {
                            (context as? Activity)?.finish()
                        }
                    }
                }

            })
    }

    if (showProgress) {
        FullScreenProgress()
    }
}