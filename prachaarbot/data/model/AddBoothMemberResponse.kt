package com.prachaarbot.data.model

import com.google.gson.annotations.SerializedName

data class AddBoothMemberResponse(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("data") var data: BoothData? = null,
    @SerializedName("message") var message: String? = null

)

data class BoothData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("admin") var admin: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

)