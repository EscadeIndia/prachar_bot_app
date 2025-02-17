package com.prachaarbot.data.model

import com.google.gson.annotations.SerializedName

data class LogOutResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("message") var message: String? = null
)


