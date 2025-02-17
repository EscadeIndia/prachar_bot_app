package com.prachaarbot.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.prachaarbot.R
import com.prachaarbot.ui.NavScreen
import com.prachaarbot.ui.component.FullScreenProgress
import com.prachaarbot.ui.screens.viewmodel.HomeViewModel
import com.prachaarbot.ui.state.ViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("Range", "CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalGlideComposeApi::class)
@ExperimentalMaterialApi
@Composable
internal fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val user = homeViewModel.getUserData()
    var showProgress by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isChecked by remember { mutableStateOf(homeViewModel.getApplicationState()) }
    var showAppInactiveConfirmation by remember { mutableStateOf(false) }

    val showSnackbar = { text: String ->
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }

    LaunchedEffect(key1 = "message body fetched") {
        homeViewModel.messageBodyState.collectLatest {
            when (it) {
                is ViewState.Error -> {
                    showProgress = false
                    showSnackbar.invoke(it.message)
                }
                is ViewState.Initial, is ViewState.Loading -> {
                    showProgress = true
                }
                is ViewState.Success -> {
                    showProgress = false
                }
            }
        }
    }

    LaunchedEffect(key1 = null) {
        homeViewModel.openUrlIntentState.collectLatest {
            when (it) {
                is ViewState.Error -> {
                    showSnackbar.invoke(it.message)
                }
                else -> {}
            }
        }
    }

    BackHandler {
        if (showAppInactiveConfirmation) {
            showAppInactiveConfirmation = false
        } else {
            (context as? Activity)?.finish()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.primaryVariant
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomNavigationBar(navController = navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // Handle click, navigate to Dialpad
                        navController.navigate("Dialpad") // Navigate to Dialpad Screen
                    },
                    backgroundColor = Color(0xFFFFC107), // Corrected Yellow color as background
                    modifier = Modifier
                        .padding(16.dp) // Add padding to the button
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.dialpad), // Your dialpad icon
                        contentDescription = "Dialpad",
                        tint = Color.Black, // Set the tint color
                        modifier = Modifier.padding(20.dp) // Apply padding to the icon
                    )

                }
            }

        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                // Your existing content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFFFDE392))
                        .padding(horizontal = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(R.drawable.img), // Replace with your image resource
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp) // Set image size to 20.dp
                    )

                    Spacer(modifier = Modifier.width(220.dp)) // Add space between the images

                    Image(
                        painter = painterResource(R.drawable.img_placeholder), // Replace with your image resource
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp) // Set image size to 20.dp
                            .padding(top = 5.dp) // Add margin-top of 5.dp
                    )
                }

                // Row for Name with Image
                Row(
                    modifier = Modifier
                        .background(Color(0xFFFDE392))
                        .height(80.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.img_placeholder), // Replace with your image resource
                                contentDescription = null,
                                modifier = Modifier
                                    .width(50.dp)
                                    .height(50.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "name",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 19.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "PARTY NAME",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 15.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }

                    Image(
                        painter = painterResource(R.drawable.ic_open_in_new_10),
                        contentDescription = null,
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                            .padding(8.dp)
                    )
                }

                // Pass the state variables to nameandswitch
                nameandswitch(
                    isChecked = isChecked,
                    onCheckedChange = { newState ->
                        if (!newState) {
                            showAppInactiveConfirmation = true
                        } else {
                            isChecked = true
                            showSnackbar("Application activated")
                        }
                    },
                    showAppInactiveConfirmation = showAppInactiveConfirmation,
                    onConfirmationDismiss = { showAppInactiveConfirmation = false },
                    onDeactivate = {
                        homeViewModel.updateApplicationActiveState(false)
                        isChecked = false
                        showAppInactiveConfirmation = false
                        showSnackbar("Application deactivated")
                    }
                )

                TodayYetc(navController = navController) // Pass the navController here
                CallStatusRow()
                MessageHeader()
                MessageCard()


                Spacer(modifier = Modifier.weight(1f))

//                if (!showAppInactiveConfirmation) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(100.dp)
//                            .padding(bottom = 50.dp), // Adding bottom margin here
//                        backgroundColor = Color(0xFFFFEF84),
//                        elevation = 10.dp,
//                        shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//
//                        ) {
//                            Icon(
//                                tint = Color.Black,
//                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_sms_16),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(18.dp)
//                                    .padding(top = 4.dp)
//                                    .clickable {
//                                        navController.navigate(NavScreen.QuickMessageScreen.route)
//                                    }
//                            )
//
//                            Text(
//                                text = "Send Quick Message",
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp,
//                                color = Color.Black,
//                                modifier = Modifier
//                                    .padding(start = 10.dp, end = 30.dp)
//                                    .clickable {
//                                        navController.navigate(NavScreen.QuickMessageScreen.route)
//                                    }
//                            )
//
//                            IconButton(
//                                modifier = Modifier
//                                    .size(16.dp),
//                                onClick = {
//                                    navController.navigate(NavScreen.Settings.route)
//                                }) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_settings_24),
//                                    contentDescription = null,
//                                    tint = Color.Black
//                                )
//                            }
//
//                            Text(
//                                text = "Settings",
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp,
//                                color = Color.Black,
//                                modifier = Modifier
//                                    .padding(start = 10.dp)
//                                    .clickable {
//                                        navController.navigate(NavScreen.Settings.route)
//                                    }
//                            )
//                        }
//                    }
//                }
            }
        }
    }

    if (showProgress) {
        FullScreenProgress()
    }
}



@Composable
fun nameandswitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    showAppInactiveConfirmation: Boolean,
    onConfirmationDismiss: () -> Unit,
    onDeactivate: () -> Unit
) {
    if (showAppInactiveConfirmation) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 50.dp)  // Added bottom padding to create margin from bottom
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                backgroundColor = Color.White,
                elevation = 10.dp,
                shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Are you sure you want to deactivate the application?",
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 30.dp)
                    )

                    Text(
                        text = "Deactivating will stop sending the message",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp)) // Added space between text and buttons

                    Row {
                        Button(
                            onClick = { onDeactivate() },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Deactivate",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Button(
                            onClick = { onConfirmationDismiss() },
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 12.dp)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween, // Space between start and end
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {

        // Hi Chandu at the start
        Text(
            text = "Hi! Chandu",
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(end = 8.dp)
        )

        // ACTIVE/DEACTIVATED at the end
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End, // Align text and switch to the end
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isChecked) "ACTIVE" else "DEACTIVATED",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = if (isChecked) Color.Black else Color.Gray
            )

            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.size(width = 60.dp, height = 30.dp), // Set width and height of the Switch
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFFFDE392),
                    checkedTrackColor = Color.Gray,
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = Color.White
                )
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        backgroundColor = Color.White
    ) {
        val items = listOf(
            BottomNavScreen.Home,
            BottomNavScreen.Analytics,
            BottomNavScreen.Settings,
            BottomNavScreen.More
        )

        // Safe check for currentRoute
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = screen.title,
                        modifier = Modifier.size(22.dp),
                        tint = Color.Black
                    )
                },
                label = {
                    Text(screen.title, color = Color.Black)
                },
                selected = currentRoute == screen.route,
                onClick = {
                    // Ensure Analytics and More are navigated correctly
                    navController.navigate(screen.route) {
                        // Prevent building up multiple copies of the same screen
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                selectedContentColor = Color.Yellow,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

sealed class BottomNavScreen(val route: String, val title: String, val icon: Int) {
    object Home : BottomNavScreen("home", "Home", R.drawable.home)
    object Analytics : BottomNavScreen("analytics", "Analytics", R.drawable.analytics)
    object Settings : BottomNavScreen("settings", "Settings", R.drawable.settings)
    object More : BottomNavScreen("more", "More", R.drawable.more)
}




@Composable
fun TodayYetc(navController: NavController) {

    var selectedDate by remember { mutableStateOf("TODAY") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
    ) {
        // "TODAY", "YESTERDAY", etc. row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White)
                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            // List of date texts to be displayed
            val dates = listOf("TODAY", "YESTERDAY", "LAST WEEK", "SELECT DATES")

            dates.forEach { date ->
                Text(
                    text = date,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            // Update selected date
                            selectedDate = date
                        }
                        .then(
                            // Set background based on selected date
                            if (selectedDate == date) {
                                Modifier.background(
                                    color = Color(0xFFFDE392), // Set background color to #FDE392
                                    shape = RoundedCornerShape(20.dp) // Rounded corners
                                )
                            } else {
                                Modifier
                            }
                        )
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        // The CallHeaderRow now follows the date section, pass navController here
        CallHeaderRow(navController = navController)
    }
}

@Composable
fun CallHeaderRow(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // "Calls" Text
        Text(
            text = "Calls",
            style = androidx.compose.ui.text.TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )

        // "View All" Text with navigation action
        Text(
            text = "View All",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 13.sp,
                color = Color(0xFFFFEB3B), // Yellow color
                fontWeight = FontWeight.Normal
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    // Navigate to the "View All" screen when tapped
                    navController.navigate("viewAll")
                }
        )
    }
}

@Composable
fun MyViewAll() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                // Home Screen Content
                Text(text = "Home Screen", modifier = Modifier.padding(paddingValues))
            }
            composable("viewAll") {
                // "View All" Screen Content
                Text(text = "View All Screen", modifier = Modifier.padding(paddingValues))
            }
        }
    }
}


@Composable
fun MessageHeader(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Message",
                style = androidx.compose.ui.text.TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            // "View All" Text
            Text(
                text = "View All",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 13.sp,
                    color = Color(0xFFFFEB3B), // Yellow color
                    fontWeight = FontWeight.Normal
                ),
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
        }
}

@Composable
fun CallStatusRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // Space between items
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Incoming Calls
        CallItem(
            imageRes = R.drawable.received_calls,
            count = "0",
            label = "Incoming"
        )

        // Outgoing Calls
        CallItem(
            imageRes = R.drawable.outgoing_calls,
            count = "0",
            label = "Outgoing"
        )

        // Missed Calls
        CallItem(
            imageRes = R.drawable.missed_calls,
            count = "0",
            label = "Missed"
        )
    }
}

@Composable
fun CallItem(imageRes: Int, count: String, label: String) {
    Row(
        modifier = Modifier
//            .padding(start = 6.dp)
            .height(60.dp)
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically, // Vertically center image, count, and label
        horizontalArrangement = Arrangement.Center // Center the content horizontally
    ) {
        // Box for image
        Box(
            modifier = Modifier
                .size(60.dp) // Set a specific size for the box
                .background(
                    color = Color(0xFFFDE392), // Set the background color using the correct hex format
                    shape = RoundedCornerShape(30.dp) // Set rounded corners
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = label,
                modifier = Modifier
                    .size(40.dp)
                    .padding(5.dp)// Adjust image size inside the box
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // Add some space between image and count/label

        // Column for count and label next to the image
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            // Display count next to the image
            Text(
                text = count,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black,
            )

            // Display label below the count
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Black, // Black color for label text
                fontWeight = FontWeight.Bold, // Bold label
            )
        }
    }
}

@Composable
fun MessageCard() {
    // Outer Column to hold all the items
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 20.dp, top = 5.dp)
            .background(
                color = Color.White, // Assuming stroke_background drawable is equivalent to a border
                shape = RoundedCornerShape(10.dp)
            )
            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)) // Border effect
    ) {
        // First Row (Top Row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Total count Text
            Text(
                text = "0",
                fontSize = 29.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(90.dp)
                    .height(60.dp)
                    .padding(3.dp),
                textAlign = TextAlign.Center
            )

            // Column for "Total" and "Messages sent"
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "Total", fontSize = 14.sp)
                Text(
                    text = "Messages sent",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Outgoing Mail Icon
            Image(
                painter = painterResource(id = R.drawable.outgoing_mail),
                contentDescription = "Outgoing Mail",
                modifier = Modifier
                    .size(70.dp) // Increased size for outgoing mail icon
                    .padding(start = 90.dp)
                    .fillMaxHeight()
            )
        }

        // Divider Line with margin at start and end
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
               // .padding(start = 15.dp, end = 15.dp, vertical = 5.dp) // Add 15dp margin at the start and end
                .background(Color.Gray), // Divider color
            thickness = 2.dp
        )


        // Second Row (WhatsApp and SMS)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp) // Adjusted row height for icons and text
                .padding(top = 10.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // WhatsApp Section (using MessageIconWithText for WhatsApp)
            MessageIconWithText(
                icon = R.drawable.ic_whatsapp,
                messageCount = 0,
                label = "WhatsApp"
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Divider between WhatsApp and SMS
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // SMS Section (using MessageIconWithText for SMS)
            MessageIconWithText(
                icon = R.drawable.message_on_recent,
                messageCount = 0,
                label = "SMS"
            )

            // Arrow Image
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_forward_12),
                contentDescription = "Arrow",
                modifier = Modifier
                    .size(40.dp) // Adjusted arrow size
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun MessageIconWithText(
    icon: Int, // Resource ID for the icon image
    messageCount: Int, // The message count to display
    label: String // Label like "WhatsApp" or "SMS"
) {
    Row(
        modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with background and rounded corners
        Box(
            modifier = Modifier
                .size(50.dp) // Icon size
                .background(Color(0xFFFDE392), shape = RoundedCornerShape(28.dp)) // Set the background color here
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp) // Add 10 dp padding around the image
            )

        }

        Spacer(modifier = Modifier.width(10.dp)) // Space between image and text

        // Column for Text beside the Image
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$messageCount", // Dynamically display the message count
                fontSize = 16.sp, // Larger font size for clarity
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = label, // Dynamically display the label
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}





//
//
//@SuppressLint("Range", "CoroutineCreationDuringComposition")
//@OptIn(ExperimentalGlideComposeApi::class)
//@ExperimentalMaterialApi
//@Composable
//internal fun HomeScreen(
//    homeViewModel: HomeViewModel = hiltViewModel(), navController: NavController
//) {
//
//    val context = LocalContext.current
//
//    val user = homeViewModel.getUserData()
//
//    var showProgress by remember {
//        mutableStateOf(false)
//    }
//
//    val scope = rememberCoroutineScope()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    var isChecked by remember { mutableStateOf(homeViewModel.getApplicationState()) }
//    var showAppInactiveConfirmation by remember {
//        mutableStateOf(false)
//    }
//    val showSnackbar = { text: String ->
//        scope.launch {
//            snackbarHostState.showSnackbar(text)
//        }
//    }
//    LaunchedEffect(key1 = "message body fetched", block = {
//        homeViewModel.messageBodyState.collectLatest {
//            when (it) {
//                is ViewState.Error -> {
//                    showProgress = false
//                    showSnackbar.invoke(it.message)
//                }
//
//                is ViewState.Initial -> {
//                    showProgress = true
//                }
//
//                is ViewState.Loading -> {
//                    showProgress = true
//                }
//
//                is ViewState.Success -> {
//                    showProgress = false
//
//                }
//            }
//        }
//    })
//    LaunchedEffect(key1 = null) {
//        homeViewModel.openUrlIntentState.collectLatest {
//            when (it) {
//                is ViewState.Error -> {
//                    showSnackbar.invoke(it.message)
//                }
//
//                is ViewState.Initial -> {}
//                is ViewState.Loading -> {}
//                is ViewState.Success -> {}
//            }
//        }
//    }
//
//
//    BackHandler {
//        if (showAppInactiveConfirmation) {
//            showAppInactiveConfirmation = false
//        } else {
//            (context as? Activity)?.finish()
//        }
//    }
//    Surface(
//        modifier = Modifier
//            .fillMaxSize(), color = MaterialTheme.colors.primaryVariant
//    ) {
//        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, backgroundColor = MaterialTheme.colors.primaryVariant, content = {
//            it.toString()
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 100.dp).height(260.dp)
//            ) {
////                Image(
////                    painter = painterResource(R.drawable.ic_homepage),
////                    contentDescription = null,
////                    modifier = Modifier
////                        .height(260.dp)
////                        .width(260.dp)
////                        .align(Alignment.Center)
////                )
//            }
//            Column(
//                verticalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Spacer(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(15.dp)
//                )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Image(
//                        modifier = Modifier
//                            .width(115.dp)
//                            .height(32.dp),
//                        painter = painterResource(R.drawable.app_header_icon),
//                        contentScale = ContentScale.FillBounds,
//                        contentDescription = ""
//                    )
//
//                    Spacer(modifier = Modifier.weight(1f))
////                Text(
////                    text = "Home",
////                    fontFamily = Monstserrat,
////                    fontWeight = FontWeight.Bold,
////                    fontSize = 18.sp,
////                    color = Black,
////                    modifier = Modifier
////
////                        .padding(start = 12.dp)
////
////                )
//
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Switch(
//                            checked = isChecked,
//                            onCheckedChange = {
//                                if (it.not()) {
//                                    showAppInactiveConfirmation = true
//                                } else {
//                                    homeViewModel.updateApplicationActiveState(true)
//                                    isChecked = true
//                                }
//                            },
//                            colors = SwitchDefaults.colors(
//                                checkedThumbColor = Color.Green,
//                                checkedTrackColor = Color.White,
//                                uncheckedThumbColor = Color.LightGray,
//                                uncheckedTrackColor = Color.White
//                            )
//                        )
//                        Text(
//                            text = if (isChecked) "ACTIVE" else "DEACTIVATED",
//                            color = Black
//                        )
//                    }
//
//                }
//
//                Spacer(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(32.dp)
//                )
//                val linearLayout = LinearLayout(this)
//                linearLayout.orientation = LinearLayout.VERTICAL
//                linearLayout.layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT
//                )
//
//// Create the first TextView
//                val nameTextView = TextView(this).apply {
//                    id = R.id.name
//                    text = "name"
//                    textStyle = Typeface.BOLD
//                    setTextColor(ContextCompat.getColor(context, R.color.black))
//                    textSize = 19f // Text size is in sp, not dp
//                    layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    )
//                }
//
//// Create the second TextView
//                val partyTextView = TextView(this).apply {
//                    id = R.id.name_of_party_on_ownpage
//                    text = "name"
//                    textStyle = Typeface.NORMAL
//                    textSize = 15f // Text size is in sp, not dp
//                    layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.WRAP_CONTENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    )
//                }
//
//// Add TextViews to LinearLayout
//                linearLayout.addView(nameTextView)
//                linearLayout.addView(partyTextView)
//
//// Create the ImageView
//                val imageView = ImageView(this).apply {
//                    layoutParams = LinearLayout.LayoutParams(
//                        40.dpToPx(), // Convert dp to px for the width
//                        LinearLayout.LayoutParams.MATCH_PARENT
//                    ).apply {
//                        marginStart = 200.dpToPx() // Convert dp to px for margin
//                    }
//                    setImageResource(R.drawable.ic_open_in_new_10)
//                    setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx()) // Convert dp to px for padding
//                }
//
//// Add ImageView to LinearLayout
//                linearLayout.addView(imageView)
//
//// Assuming you are adding this linearLayout to a parent view in your activity
//                parentLayout.addView(linearLayout)
//
////                Card(
////                    backgroundColor = MaterialTheme.colors.primary,
////                    elevation = 10.dp,
////                    shape = RoundedCornerShape(16.dp),
////                    modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(horizontal = 16.dp),
////                    onClick = {
////                        homeViewModel.openUserProfileIntent()
////                    },
////
////                    ) {
////                    Column {
////                        Row(
////                            modifier = Modifier
////                                .fillMaxWidth()
////                                .padding(12.dp),
////                            verticalAlignment = Alignment.CenterVertically
////                        ) {
////                            GlideImage(
////                                requestBuilderTransform = {
////                                    it.circleCrop()
////                                },
////                                contentScale = ContentScale.FillBounds,
////                                modifier = Modifier
////                                    .height(61.dp)
////                                    .width(61.dp)
////                                    .border(
////                                        1.dp, Grey, shape = CircleShape
////                                    ),
////
////                                model = user?.imageUrl,
////                                // Shows a placeholder while loading the image.
////                                loading = placeholder(painterResource(R.drawable.logo_prachaarbot)),
////                                failure = placeholder(painterResource(R.drawable.logo_prachaarbot)),
////                                contentDescription = ""
////                            )
////                            Column(
////                                modifier = Modifier
////                                    .weight(1f)
////                                    .padding(start = 16.dp)
////                            ) {
////                                Text(
////                                    text = user?.name ?: "",
////                                    fontFamily = Monstserrat,
////                                    fontWeight = FontWeight.Bold,
////                                    fontSize = 16.sp,
////                                    color = Black,
////                                )
////                                Text(
////                                    text = user?.party ?: "",
////                                    fontFamily = Monstserrat,
////                                    fontWeight = FontWeight.Normal,
////                                    fontSize = 12.sp,
////                                    color = Color(0xFF211111),
////                                    modifier = Modifier.padding(top = 6.dp)
////
////                                )
////
////                                Text(
////                                    text = user?.designation ?: "",
////                                    fontFamily = Monstserrat,
////                                    fontWeight = FontWeight.Normal,
////                                    fontSize = 12.sp,
////                                    color = Color(0xFF211111),
////                                    modifier = Modifier.padding(top = 6.dp)
////
////                                )
////                            }
////                        }
////                        Button(
////                            modifier = Modifier
////                                .fillMaxWidth()
////                                .height(33.dp),
////                            onClick = {
////                                homeViewModel.openUserProfileIntent()
////                            },
////                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFEF84))
////                        ) {
////                            Row(verticalAlignment = Alignment.CenterVertically) {
////                                Text(
////                                    text = "VIEW PROFILE",
////                                    fontFamily = Monstserrat,
////                                    fontWeight = FontWeight.Normal,
////                                    fontSize = 12.sp,
////                                    color = Black,
////                                    modifier = Modifier
////                                )
////                                Icon(
////                                    tint = Black,
////                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_open_in_new_10),
////                                    contentDescription = null,
////                                    modifier = Modifier
////                                        .size(24.dp)
////                                        .padding(start = 10.dp)
////
////                                )
////
////                            }
////                        }
////
////                    }
////
////                }
//
//
//                Spacer(modifier = Modifier.weight(1f))
//                AnimatedVisibility(
//                    visible = showAppInactiveConfirmation,
//                    enter = slideInVertically(initialOffsetY = { it }),
//                    exit = slideOutVertically(targetOffsetY = { it }),
//
//                    ) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        backgroundColor = Color.White,
//                        elevation = 10.dp,
//                        shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .padding(horizontal = 16.dp)
//                        ) {
//
//
//                            Text(
//                                text = "Are you sure you want to deactivate the application?",
//                                fontFamily = Segoe,
//                                fontWeight = FontWeight.Normal,
//                                fontSize = 18.sp,
//                                color = Black,
//                                modifier = Modifier
//                                    .padding(top = 30.dp)
//                                    .align(alignment = Alignment.Start)
//                            )
//
//                            Text(
//                                text = "Deactivating will stop sending the message",
//                                fontFamily = Segoe,
//                                fontWeight = FontWeight.Normal,
//                                fontSize = 12.sp,
//                                color = Grey,
//                                modifier = Modifier
//                                    .padding(top = 10.dp)
//                                    .align(alignment = Alignment.Start)
//                            )
//                            Row {
//                                Button(
//                                    onClick = {
//                                        homeViewModel.updateApplicationActiveState(false)
//                                        showAppInactiveConfirmation = false
//                                        isChecked = false
//                                    },
//                                    modifier = Modifier
//                                        .weight(1f)
//                                        .padding(vertical = 24.dp)
//                                        .height(48.dp),
//                                    colors = ButtonDefaults.buttonColors(backgroundColor = Black),
//                                    shape = RoundedCornerShape(8.dp)
//                                ) {
//                                    Text(
//                                        text = "Deactivate",
//                                        fontFamily = Segoe,
//                                        fontWeight = FontWeight.Normal,
//                                        fontSize = 14.sp,
//                                        color = Color.White
//                                    )
//                                }
//                                Spacer(modifier = Modifier.width(15.dp))
//                                Button(
//                                    onClick = {
//                                        showAppInactiveConfirmation = false
//
//                                    },
//                                    modifier = Modifier
//                                        .weight(1f)
//                                        .padding(vertical = 24.dp)
//                                        .height(48.dp),
//                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
//                                    shape = RoundedCornerShape(8.dp)
//                                ) {
//                                    Text(
//                                        text = "Cancel",
//                                        fontFamily = Segoe,
//                                        fontWeight = FontWeight.Normal,
//                                        fontSize = 14.sp,
//                                        color = Grey
//                                    )
//                                }
//                            }
//
//                        }
//
//                    }
//                }
//                if (showAppInactiveConfirmation.not()) {
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(70.dp),
//                        backgroundColor = Color(0xFFFFEF84),
//                        elevation = 10.dp,
//                        shape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp)
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                tint = Black,
//                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_sms_16),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(18.dp)
//                                    .padding(top = 4.dp)
//                                    .clickable {
//                                        navController.navigate(NavScreen.QuickMessageScreen.route)
//                                    }
//
//                            )
//                            Text(
//                                text = "Send Quick Message",
//                                fontFamily = Monstserrat,
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp,
//                                color = Black,
//                                modifier = Modifier
//                                    .padding(start = 10.dp, end = 30.dp)
//                                    .clickable {
//                                        navController.navigate(NavScreen.QuickMessageScreen.route)
//                                    }
//
//                            )
//
//                            IconButton(
//                                modifier = Modifier
//                                    .size(16.dp),
//                                onClick = {
//                                    navController.navigate(NavScreen.Settings.route)
//                                }) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_settings_24), "",
//                                    tint = Black
//                                )
//                            }
//                            Text(
//                                text = "Settings",
//                                fontFamily = Monstserrat,
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp,
//                                color = Black,
//                                modifier = Modifier
//                                    .padding(start = 10.dp)
//                                    .clickable {
//                                        navController.navigate(NavScreen.Settings.route)
//                                    }
//
//                            )
//                        }
//
//                    }
//                }
//
//            }
//        })
//
//    }
//    if (showProgress) {
//        FullScreenProgress()
//    }
//
//}