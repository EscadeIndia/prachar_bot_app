package com.prachaarbot.data.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

data class Permissions(
    val displayName: String, val permission: String, val resourceImage: Int? = null, var isEnabled: Boolean,
    val extraDetails: String? = ""
)


private const val EXTRA_PERMISSIONS = "androidx.activity.result.contract.extra.PERMISSIONS"
private const val ACTION_REQUEST_PERMISSIONS =
    "androidx.activity.result.contract.action.REQUEST_PERMISSIONS"

internal class MyPermissionContract : ActivityResultContract<Permissions, Permissions>() {

    private var localPermission: Permissions? = null
    override fun createIntent(context: Context, input: Permissions): Intent {
        localPermission = input
        return Intent(ACTION_REQUEST_PERMISSIONS).putExtra(EXTRA_PERMISSIONS, arrayOf(input.permission))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Permissions {
        if (intent == null || resultCode != Activity.RESULT_OK) return localPermission?.apply {
            isEnabled = false
        } ?: throw IllegalStateException("localPermission must not be null.")
        val grantResults =
            intent.getIntArrayExtra(ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS)
        return localPermission?.apply {
            isEnabled = grantResults?.any { result ->
                result == PackageManager.PERMISSION_GRANTED
            } == true
        } ?: throw IllegalStateException("localPermission must not be null.")
    }

    override fun getSynchronousResult(
        context: Context,
        input: Permissions
    ): SynchronousResult<Permissions>? {
        val granted = ContextCompat.checkSelfPermission(
            context,
            input.permission
        ) == PackageManager.PERMISSION_GRANTED
        return if (granted) {
            SynchronousResult(input)
        } else {
            // proceed with permission request
            null
        }
    }
}
