package com.prachaarbot.ui

sealed class NavScreen(val route: String) {
    object Splash : NavScreen(route = "Splash")
    object SignIn : NavScreen(route = "SignIn")
    object Home : NavScreen(route = "Home")
    object Settings : NavScreen(route = "Settings")
    object Permission : NavScreen(route = "Permission")
    object ProfileType : NavScreen(route = "ProfileType")
    object QuickMessageScreen : NavScreen(route = "QuickMessageScreen")
    object BlockedContactScreen : NavScreen(route = "BlockedContactScreen")
    object RecentCallsScreen : NavScreen(route = "RecentCallsScreen")
    object AnalyticsScreen : NavScreen(route = "Analytics")   // Analytics route
    object MoreScreen : NavScreen(route = "More")
    object ViewallScreen : NavScreen(route = "ViewAll")
    object DialpadScreen : NavScreen(route = "Dialpad")

// More route
}

