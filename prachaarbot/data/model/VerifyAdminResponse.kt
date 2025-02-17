package com.prachaarbot.data.model

import com.google.gson.annotations.SerializedName

data class VerifyAdminResponse(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("data") var data: Any? = null,
    @SerializedName("message") var message: String? = null

)
