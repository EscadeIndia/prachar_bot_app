package com.prachaarbot.data.model

import com.google.gson.annotations.SerializedName


data class GetMessageBodyResponse(

    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("data") var data: Data? = Data(),
    @SerializedName("message") var message: String? = null

)

data class Data(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("admin") var admin: String? = null,
    @SerializedName("img") var img: String? = null,
    @SerializedName("video") var video: String? = null,
    @SerializedName("news") var news: ArrayList<News> = arrayListOf(),
    @SerializedName("whats_msg") var whatsMsg: String? = null,
    @SerializedName("text_msg") var textMsg: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

)

data class News(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("img") var img: String? = null,
    @SerializedName("img_alt") var imgAlt: String? = null,
    @SerializedName("admin") var admin: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null

)