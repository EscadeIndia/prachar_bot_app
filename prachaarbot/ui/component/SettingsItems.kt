package com.prachaarbot.ui.component

import android.Manifest
import android.content.pm.PackageManager
import android.telephony.SubscriptionManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat
import com.prachaarbot.ui.theme.getDefaultColors


@Composable
internal fun ToggleFeature(
    heading: String,
    subHeading: String,
    currentState: Boolean,
    onStateChange: (state: Boolean) -> Unit
) {

    var toggleState by remember { mutableStateOf(currentState) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = heading,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Black,
                fontFamily = Monstserrat,
                modifier = Modifier
                    .weight(1f)
            )
            Switch(
                modifier = Modifier.height(30.dp),
                colors = SwitchDefaults.colors(
                    uncheckedThumbColor = Color(0xFFFFC600),
                    checkedThumbColor = Color(0xFFFFC600),
                    checkedTrackColor = Color(0xFFFFEEB4),
                    uncheckedTrackColor = Grey
                ), checked = toggleState, onCheckedChange = {
                    toggleState = it
                    onStateChange.invoke(it)

                })
        }
        Text(
            text = subHeading,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grey,
            fontFamily = Monstserrat,
        )

    }
}

@Composable
internal fun SliderFeature(
    heading: String,
    subHeading: String,
    initialValue: Int,
    onStateChange: (state: Int) -> Unit
) {

    var sliderPosition by remember { mutableStateOf(initialValue.toFloat()) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp),

        ) {
        Text(
            text = heading,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Black,
            fontFamily = Monstserrat,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = subHeading,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Grey,
            fontFamily = Monstserrat,
        )

        Text(
            text = "${sliderPosition.toInt()}/15",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Black,
            fontFamily = Monstserrat,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp),
        )
        Slider(
            colors = SliderDefaults.getDefaultColors(),
            value = sliderPosition,
            valueRange = 1f..15f,
            onValueChange = { newValue ->
                sliderPosition = newValue
                onStateChange.invoke(newValue.toInt())
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        )

    }
}

@Composable
internal fun SimSelectorFeature(
    heading: String,
    initialValue: Int,
    onStateChange: (state: Int) -> Unit
) {

    val permission = Manifest.permission.READ_PHONE_STATE
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->

    }
    if (ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED
    ) {

        val options = mutableListOf<String>()
        ContextCompat.getSystemService(
            context,
            SubscriptionManager::class.java
        )?.activeSubscriptionInfoList?.let {
            it.getOrNull(0)?.let { subsInfo ->
                options.add(subsInfo.displayName.toString())
            }
            it.getOrNull(1)?.let { subsInfo ->
                options.add(subsInfo.displayName.toString())
            }
        }
        var selectedOption by remember { mutableStateOf(options.getOrNull(initialValue)?: "") }

        Column(
            modifier = Modifier.padding(top = 30.dp)
        ) {
            Text(
                heading,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Black,
                fontFamily = Monstserrat,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                options.forEachIndexed { index, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 30.dp).clickable {
                            selectedOption = option
                            onStateChange.invoke(index)
                        }

                    ) {
                        RadioButton(
                            modifier = Modifier.size(16.dp),
                            selected = (option == selectedOption),
                            onClick = {
                                selectedOption = option
                                onStateChange.invoke(index)
                            }
                        )
                        Text(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (selectedOption == option) Black else Grey,
                            fontFamily = Monstserrat,
                            text = if (selectedOption == option) "$option (Default)" else option,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }

            }
        }
    } else {
        //requestPermissionLauncher.launch(permission)

    }

}


@Composable
internal fun MessageLimitSetterFeature(
    heading: String,
    subHeading: String,
    initialValue: Int,
    onStateChange: (state: Int) -> Unit
) {

    var maxCount by remember { mutableStateOf(initialValue) }

    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = heading,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Black,
                fontFamily = Monstserrat,
            )
            Text(
                text = subHeading,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grey,
                fontFamily = Monstserrat,

                )
        }

        Row(
            Modifier.border(
                BorderStroke(1.dp, Grey), shape = RoundedCornerShape(8.dp)
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            // Decrement button
            IconButton(
                onClick = {
                    if (maxCount > 0) {
                        maxCount--
                        onStateChange.invoke(maxCount)
                    }
                },
                modifier = Modifier
                    .size(38.dp)
                    .background(Color.Transparent)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrement", tint = Grey)
            }

            Text(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Grey,
                fontFamily = Monstserrat,
                text = "$maxCount"
            )

            // Increment button
            IconButton(
                onClick = {
                    if (maxCount < 15) {
                        maxCount++
                        onStateChange.invoke(maxCount)
                    }
                },
                modifier = Modifier
                    .size(38.dp)
                    .background(Color.Transparent)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increment", tint = Grey)
            }
        }
    }
}

@Composable
internal fun AddExtraSpaceWithDividerInBetween(space: Dp = 18.dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(space)
    )
    Divider(
        color = Grey,
        thickness = 0.5.dp
    )
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(space)
    )
}