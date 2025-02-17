package com.prachaarbot.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.prachaarbot.R
import com.prachaarbot.ui.NavScreen
import com.prachaarbot.ui.screens.viewmodel.SplashViewModel
import com.prachaarbot.ui.state.LoginState
import kotlinx.coroutines.delay

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalAnimationApi
@Composable
internal fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navController: NavController
) {

    val permissionsList = mutableListOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.VIBRATE
    )
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
    val permissionsState = rememberMultiplePermissionsState(permissionsList)

    LaunchedEffect(key1 = permissionsState, block = {
        permissionsState.launchMultiplePermissionRequest()
    })

    splashViewModel.checkForLoginState()

    LaunchedEffect(key1 = null, block = {
        delay(2000)
        splashViewModel.loginStatus.collect {
            when (it) {
                LoginState.Loading -> {

                }

                LoginState.NotLoggedIn -> {
                    navController.popBackStack()
                    navController.navigate(NavScreen.SignIn.route)
                }

                LoginState.LoggedIn -> {
                    navController.popBackStack()
                    if (splashViewModel.usersProfileTypeIsSet()) {
                        navController.navigate(NavScreen.Home.route)
                    } else {
                        navController.navigate(NavScreen.ProfileType.route)
                    }
                }
            }
        }
    })

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier.background(color = MaterialTheme.colors.primary)
        ) {
            val (box, image) = createRefs()
            Box(
                modifier = Modifier
                    .constrainAs(box) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                    }
                    .size(700.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFC800), Color(0xFFFFDD00)),
                        )
                    )
            )

            Image(
                painter = painterResource(R.drawable.logo_prachaarbot),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        absoluteLeft.linkTo(parent.absoluteLeft)
                        absoluteRight.linkTo(parent.absoluteRight)
                    }
                    .height(163.dp)
                    .width(184.dp)
            )
        }
    }

}