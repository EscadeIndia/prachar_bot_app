package com.prachaarbot.utils

fun formatPhoneNumber(phoneNumber: String): String? {
    // Remove any spaces or dashes from the input
    val cleanedNumber = phoneNumber.replace(" ", "").replace("-", "")

    // Check if the cleaned number starts with "+91"
    return when {
        cleanedNumber.startsWith("+91") -> {
            // If the size is 13, keep it as is
            if (cleanedNumber.length == 13) {
                cleanedNumber
            } else {
                null
            }
        }

        else -> {
            // If the size is 10, add "+91" as the prefix
            if (cleanedNumber.length == 10) {
                "+91$cleanedNumber"
            } else {
                null
            }
        }
    }
}
