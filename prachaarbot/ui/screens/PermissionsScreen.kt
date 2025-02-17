package com.prachaarbot.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prachaarbot.R
import com.prachaarbot.accessibilityservice.WhatsAppAccessibilityService
import com.prachaarbot.accessibilityservice.hasAccessibilityServicePermission
import com.prachaarbot.data.model.MyPermissionContract
import com.prachaarbot.data.model.Permissions
import com.prachaarbot.ui.component.OtherPermissionsItems
import com.prachaarbot.ui.component.RequiredPermissionsItems
import com.prachaarbot.ui.screens.viewmodel.PermissionsViewModel
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Grey
import com.prachaarbot.ui.theme.Monstserrat
import kotlinx.coroutines.launch

@Composable
internal fun PermissionsScreen(
    navController: NavController, permissionsViewModel: PermissionsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    var newPermissionGranted by remember {
        mutableIntStateOf(1)
    }

    var showAccessibilityPolicy by remember {
        mutableStateOf(false)
    }

    val permissions = listOf(
        Permissions(
            "Call logs",
            Manifest.permission.READ_CALL_LOG,
            R.drawable.ic_phone_logs_16,
            permissionsViewModel.isPermissionGranted(Manifest.permission.READ_CALL_LOG)
        ),
        Permissions(
            "Contacts",
            Manifest.permission.READ_CONTACTS,
            R.drawable.ic_contacts_16,
            permissionsViewModel.isPermissionGranted(Manifest.permission.READ_CONTACTS)
        ),
        Permissions(
            "Phone",
            Manifest.permission.READ_PHONE_STATE,
            R.drawable.ic_call_16,
            permissionsViewModel.isPermissionGranted(Manifest.permission.READ_PHONE_STATE)
        ),
        Permissions(
            "SMS",
            Manifest.permission.SEND_SMS,
            R.drawable.ic_sms_16,
            permissionsViewModel.isPermissionGranted(Manifest.permission.SEND_SMS)
        ),
        Permissions(
            "Storage",
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            R.drawable.ic_storage_16,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) true else permissionsViewModel.isPermissionGranted(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ),
    )

    val otherPermissions = listOf(
        Permissions(
            "Accessibility to auto send WhatsApp",
            "",
            isEnabled = hasAccessibilityServicePermission(
                context, WhatsAppAccessibilityService::class.java
            ),
            extraDetails = "Required for auto sending WhatsApp messages."
        ), Permissions(
            "Allow unrestricted battery access",
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            isEnabled = permissionsViewModel.isPermissionGranted(Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        ), Permissions(
            "Display over other apps/popup screen",
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            isEnabled = true, //permissionsViewModel.isPermissionGranted(Manifest.permission.SYSTEM_ALERT_WINDOW),
            extraDetails = "Required for auto sending WhatsApp messages."
        )
    )

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            otherPermissions.get(0).isEnabled = hasAccessibilityServicePermission(
                context, WhatsAppAccessibilityService::class.java
            )
            if (result.resultCode == Activity.RESULT_OK) {
                newPermissionGranted += 1
            }
            if (result.resultCode == Activity.RESULT_CANCELED && hasAccessibilityServicePermission(
                    context, WhatsAppAccessibilityService::class.java
                )
            ) {
                newPermissionGranted += 1
            }
        }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = MyPermissionContract(),
    ) { _ ->
        newPermissionGranted += 1
    }

    val requestOtherPermissionLauncher = rememberLauncherForActivityResult(
        contract = MyPermissionContract(),
    ) { _ ->
        newPermissionGranted += 1
    }
    Surface(
        modifier = Modifier.fillMaxSize(), color = Color.White
    ) {
        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, content = {
            it.toString()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    IconButton(modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                        .padding(top = 2.dp)
                        .align(Alignment.CenterVertically), onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack, contentDescription = ""
                        )
                    }
                    Text(
                        text = "Permissions",
                        fontFamily = Monstserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Black,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)

                    )
                }
                if (newPermissionGranted > 0) {
                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        item {
                            Text(
                                text = "REQUIRED PERMISSIONS",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Grey,
                                fontFamily = Monstserrat,
                                modifier = Modifier.padding(top = 32.dp)
                            )
                        }
                        items(permissions) { permission ->
                            RequiredPermissionsItems(permission = permission, showSnack = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(it)
                                }
                            }, onToggle = {
                                requestPermissionLauncher.launch(permission)
                            })
                        }

                        item {
                            Text(
                                text = "OTHER APP PERMISSIONS",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Grey,
                                fontFamily = Monstserrat,
                                modifier = Modifier.padding(top = 32.dp, bottom = 5.dp)
                            )
                        }
                        itemsIndexed(otherPermissions) { index, permission ->
                            OtherPermissionsItems(permission = permission, showSnack = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(it)
                                }
                            }, onToggle = {
                                if (it) {
                                    requestOtherPermissionLauncher.launch(permission)
                                } else {
                                    if (permissionsViewModel.getPrivacyDisclosureShown()) {
                                        if (hasAccessibilityServicePermission(
                                                context, WhatsAppAccessibilityService::class.java
                                            ).not()
                                        ) {
                                            val intent =
                                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                                            context.startActivity(intent)
                                            launcher.launch(intent)
                                        }
                                    } else {
                                        showAccessibilityPolicy = true
                                    }
                                }

                            })
                            if (index < otherPermissions.lastIndex) {
                                Divider(
                                    color = Grey,
                                    thickness = 0.5.dp,
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                )
                            }
                        }
                    }
                }
            }
            if (showAccessibilityPolicy) {
                AccessibilityPolicyDialog { result ->
                    if (result) {
                        permissionsViewModel.updatePrivacyDisclosureShown()
                        if (hasAccessibilityServicePermission(
                                context, WhatsAppAccessibilityService::class.java
                            ).not()
                        ) {
                            val intent =
                                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            context.startActivity(intent)
                            launcher.launch(intent)
                        }
                    }
                    showAccessibilityPolicy = false
                }
            }
        })

    }
}

