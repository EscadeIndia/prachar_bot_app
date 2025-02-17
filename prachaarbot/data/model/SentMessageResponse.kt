package com.prachaarbot.data.model

import com.google.gson.annotations.SerializedName

data class SentMessageResponse(
    @SerializedName("success") var success: Boolean? = null,
)
