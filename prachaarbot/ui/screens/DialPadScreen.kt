package com.prachaarbot.ui.screens
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.prachaarbot.R
import com.prachaarbot.ui.screens.viewmodel.DialViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.res.painterResource



@Composable
fun DialPadScreen(navController: NavHostController) {
    val viewModel: DialViewModel = viewModel() // Get the ViewModel
    var enteredNumber by remember { mutableStateOf("+91 ") }  // Store the entered number
    val contactName by viewModel.contactName.observeAsState("Unknown")  // Observe the contact name

    // Call button click handler
    val onCallClick: () -> Unit = {
        if (enteredNumber.length > 3) {
            val phoneNumber = enteredNumber.trim()  // Ensure no leading/trailing spaces
            val dialIntent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            // Start the call activity (check permissions before doing so)
        }
    }

    // Handle backspace click
    val onBackspaceClick: () -> Unit = {
        if (enteredNumber.length > 3) {
            enteredNumber = enteredNumber.dropLast(1) // Remove the last character
        }
    }

    // Function to append number
    fun appendNumber(digit: String) {
        enteredNumber += digit
    }

    // Layout content
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                //.background(Color.LightGray) // Background color for the entire screen
        ) {
            // Header with the back button and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Dial a Call",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            // Contact name
            Text(
                text = contactName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)  // Horizontally center the contact name
            )

            // Entered number
            Text(
                text = enteredNumber,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Dialpad buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                //.padding(start = 15.dp, end = 15.dp)
            ) {
                // First row: buttons 1, 2, 3
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialpadButton(
                        digit = "1",
                        onClick = { appendNumber("1") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray) // Light gray background
                    )
                    DialpadButton(
                        digit = "2",
                        onClick = { appendNumber("2") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                    DialpadButton(
                        digit = "3",
                        onClick = { appendNumber("3") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp)) // Add spacing between rows

                // Second row: buttons 4, 5, 6
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialpadButton(
                        digit = "4",
                        onClick = { appendNumber("4") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                    DialpadButton(
                        digit = "5",
                        onClick = { appendNumber("5") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                    DialpadButton(
                        digit = "6",
                        onClick = { appendNumber("6") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Third row: buttons 7, 8, 9
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialpadButton(
                        digit = "7",
                        onClick = { appendNumber("7") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                    DialpadButton(
                        digit = "8",
                        onClick = { appendNumber("8") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                    DialpadButton(
                        digit = "9",
                        onClick = { appendNumber("9") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Fourth row: Add, 0, Backspace
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DialpadImageButton(
                        imageRes = R.drawable.add,
                        onClick = { appendNumber("+") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color(0xFFDEDEDE)) // Light gray background for "Backspace"
                    )
                    DialpadButton(
                        digit = "0",
                        onClick = { appendNumber("0") },
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color.LightGray)
                    )
                    DialpadImageButton(
                        imageRes = R.drawable.backspace, // Replace with your image resource for "Backspace"
                        onClick = onBackspaceClick,
                        modifier = Modifier
                            .width(100.dp)
                            .height(70.dp)
                            .background(Color(0xFFDEDEDE)) // Light gray background for "Backspace"
                    )
                }
            }

            // Icon row at the bottom (if necessary)
            IconRow(
                onBackspaceClick = onBackspaceClick,
                onCallClick = onCallClick
            )
        }

    }
}

@Composable
fun DialpadButton(digit: String, onClick: (String) -> Unit, modifier: Modifier) {
    Button(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp),
        onClick = { onClick(digit) },
       // shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
    ) {
        Text(
            text = digit,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}

@Composable
fun IconRow(
    onBackspaceClick: () -> Unit,
    onCallClick: () -> Unit,
    modifier: Modifier = Modifier // Allow passing of the modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp) // Set the height of the entire Row
            .padding(bottom = 10.dp), // Padding at the bottom
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically // Vertically center the icons
    ) {
        // Backspace Icon from drawable
        IconButton(onClick = onBackspaceClick) {
            Image(
                painter = painterResource(id = R.drawable.contact),
                contentDescription = "Contacts",
                modifier = Modifier
                    .width(100.dp)
                    .height(70.dp)

                    .background(Color(0xFFDEDEDE)) // Light gray background for "Backspace"
            )
        }

        // Contacts Icon from drawable
        IconButton(onClick = { /* Open contacts functionality */ }) {
            Image(
                painter = painterResource(id = R.drawable.call), // Replace with your drawable resource
                contentDescription = "Call",
                modifier = Modifier
                    .width(100.dp) // Set width for the icon
                    .height(70.dp) // Set height for the icon
                    .background(Color(0xFFFFC107)) // Yellow background for "Contacts"
            )
        }

        // Call Icon from drawable
        IconButton(onClick = onCallClick) {
            Image(
                painter = painterResource(id = R.drawable.call_log), // Replace with your drawable resource
                contentDescription = "Call log",
                modifier = Modifier
                    .width(100.dp) // Set width for the icon
                    .height(70.dp) // Set height for the icon
                    .background(Color(0xFFDEDEDE)) // Light gray background for "Call"
            )
        }
    }
}


@Composable
fun DialpadImageButton(imageRes: Int, onClick: () -> Unit, modifier: Modifier) {
    IconButton(onClick = onClick) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = modifier
                .width(100.dp)  // Set width to 100dp
                .height(70.dp)  // Set height to 70dp
        )
    }
}
