package com.prachaarbot.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prachaarbot.ui.NavScreen
import com.prachaarbot.ui.theme.PracharBotTheme


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SplashScreenApp() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    PracharBotTheme {
        Scaffold(scaffoldState = scaffoldState, content = {
            NavHost(navController = navController, startDestination = NavScreen.Splash.route) {
                composable(NavScreen.Splash.route) {
                    SplashScreen(navController = navController)
                }
                composable(NavScreen.SignIn.route) {
                    LoginScreen(navController = navController)
                }
                composableWithTransition(NavScreen.ProfileType.route) {
                    ProfileTypeScreen(navController = navController)
                }
                composableWithTransition(NavScreen.Home.route) {
                    HomeScreen(navController = navController)
                }
                composableWithTransition(NavScreen.Settings.route) {
                    SettingsScreen(navController = navController)
                }
                composableWithTransition(NavScreen.Permission.route) {
                    PermissionsScreen(navController = navController)
                }
                composable(NavScreen.QuickMessageScreen.route) {
                    QuickMessageScreen(navController = navController)
                }
                composable(NavScreen.BlockedContactScreen.route) {
                    BlockedContactScreen(navController = navController)
                }
                composable(NavScreen.RecentCallsScreen.route) {
                    RecentCallsScreen(navController = navController)
                }

                // New screens with transitions
                composableWithTransition(NavScreen.AnalyticsScreen.route) {
                    AnalyticsScreen(navController = navController)
                }
                composableWithTransition(NavScreen.MoreScreen.route) {
                    MoreScreen(navController = navController)
                }
                composableWithTransition(NavScreen.ViewallScreen.route) {
                    ViewAllScreen(navController = navController )
                }
                composableWithTransition(NavScreen.DialpadScreen.route) {
                    DialPadScreen(navController = navController )
                }
            }

        })
    }
}

fun NavGraphBuilder.composableWithTransition(
    route: String, content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable(
    route,
    enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(700)
        )
    },
    exitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(700)
        )
    },
    popEnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(700)
        )
    },
    popExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(700)
        )
    },
    content = content
)
