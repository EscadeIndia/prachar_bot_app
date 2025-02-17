package com.prachaarbot.accessibilityservice

import android.accessibilityservice.AccessibilityService
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class WhatsAppAccessibilityService : AccessibilityService() {

    @Inject
    lateinit var localPrefData: LocalPrefData
    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (localPrefData.autoSendMessageFlag) {
            Handler(Looper.getMainLooper()).postDelayed({
                localPrefData.autoSendMessageFlag = false
            }, 2500)
        val packageName = event.packageName.toString()
        if (packageName == "com.whatsapp" || packageName == "com.whatsapp.w4b") {
            try {
                try {
                    val rootNodeInfo = AccessibilityNodeInfoCompat.wrap(
                        rootInActiveWindow
                    )
                    val id = if(packageName =="com.whatsapp") "com.whatsapp:id/send" else "com.whatsapp.w4b:id/send"
                    val sendMessageNodeList =
                        rootNodeInfo.findAccessibilityNodeInfosByViewId(id)
                    if (sendMessageNodeList == null || sendMessageNodeList.isEmpty()) {
                        return
                    }
                       val sendMessage = sendMessageNodeList[0]
                       if (!sendMessage.isVisibleToUser) {
                           return
                       }
                    sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    try {
                        Thread.sleep(800)
                        performGlobalAction(GLOBAL_ACTION_BACK)
                        Thread.sleep(500)
                    } catch (e: Exception) {
                        Log.e("onAccessibilityEvent", "onAccessibilityEvent: ", e)
                    }
                    performGlobalAction(GLOBAL_ACTION_BACK)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}

override fun onInterrupt() {}

}

