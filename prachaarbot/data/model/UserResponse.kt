package com.prachaarbot.data.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("data") var data: UserData? = null,
    @SerializedName("message") var message: String? = null
)

data class UserData(
    @SerializedName("user") var user: User? = null,
    @SerializedName("token") var token: String? = null
)
data class User(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("user_name") var userName: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("email_verified_at") var emailVerifiedAt: String? = null,
    @SerializedName("website") var website: String? = null,
    @SerializedName("constituency") var constituency: String? = null,
    @SerializedName("about") var about: String? = null,
    @SerializedName("fb_url") var fbUrl: String? = null,
    @SerializedName("insta_url") var instaUrl: String? = null,
    @SerializedName("twitter_url") var twitterUrl: String? = null,
    @SerializedName("youtube_url") var youtubeUrl: String? = null,
    @SerializedName("other_url") var otherUrl: String? = null,
    @SerializedName("admin") var admin: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("party") var party: String? = null,
    @SerializedName("designation") var designation: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("img_url") var imageUrl: String? = null,
    @SerializedName("secret_code") var secretText: String? = null,
    @SerializedName("profile_url") var profileUrl: String? = null

    )
