package com.prachaarbot.ui.screens.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
internal class PermissionsViewModel
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val localPrefData: LocalPrefData
) : ViewModel() {
    fun isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(
        context, permission
    ) == PackageManager.PERMISSION_GRANTED


    fun getPrivacyDisclosureShown() = localPrefData.accessibilityDisclosureShown
    fun updatePrivacyDisclosureShown() {
        localPrefData.accessibilityDisclosureShown = true
    }
}