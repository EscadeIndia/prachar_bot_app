package com.prachaarbot.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prachaarbot.data.model.Permissions
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat

@Composable
fun RequiredPermissionsItems(
    permission: Permissions, onToggle: () -> Unit, showSnack: (String) -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        if (permission.resourceImage != null) {
            Image(
                painter = painterResource(permission.resourceImage),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
            )
        }
        Text(
            text = permission.displayName,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Black,
            fontFamily = Monstserrat,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Switch(colors = SwitchDefaults.colors(
            uncheckedThumbColor = Color(0xFFFFC600),
            checkedThumbColor = Color(0xFFFFC600),
            checkedTrackColor = Color(0xFFFFEEB4),
            uncheckedTrackColor = Grey
        ), checked = permission.isEnabled, onCheckedChange = {
            if (permission.isEnabled.not()) {
                onToggle.invoke()
            } else {
                showSnack.invoke("Go to app settings to disable the permission")
            }
        })
    }
}


@Composable
fun OtherPermissionsItems(permission: Permissions, onToggle: (isForPermission: Boolean) -> Unit, showSnack: (String) -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 60.dp),

        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = permission.displayName,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Black,
                fontFamily = Monstserrat,
                modifier = Modifier.weight(1f)
            )
            Switch(colors = SwitchDefaults.colors(
                uncheckedThumbColor = Color(0xFFFFC600),
                checkedThumbColor = Color(0xFFFFC600),
                checkedTrackColor = Color(0xFFFFEEB4),
                uncheckedTrackColor = Grey
            ), checked = permission.isEnabled, onCheckedChange = {
                if (permission.isEnabled.not() && permission.permission.isNotBlank()) {
                    onToggle.invoke(true)
                } else if (permission.permission.isBlank()) {
                    onToggle.invoke(false)
                } else {
                    showSnack.invoke("Go to app settings to disable the permission")
                }
            })
        }

        if (permission.extraDetails.isNullOrBlank().not()) {
            Text(
                text = permission.extraDetails!!,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Grey,
                fontFamily = Monstserrat,
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
        }

    }
}