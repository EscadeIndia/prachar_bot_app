package com.prachaarbot.data.remote

import com.prachaarbot.data.model.LogOutResponse
import com.prachaarbot.data.model.AddBoothMemberResponse
import com.prachaarbot.data.model.UserResponse
import com.prachaarbot.data.model.VerifyAdminResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


internal interface LoginApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("user_name") userName: String?,
        @Field("password") password: String?,
    ): UserResponse

    @POST("logout")
    suspend fun logOut(): LogOutResponse


    @FormUrlEncoded
    @POST("addboothmember")
    suspend fun addMember(
        @Field("name") name: String?,
        @Field("mobile") number: String?,
        @Field("user_id") id: String?
    ): AddBoothMemberResponse

    @FormUrlEncoded
    @POST("verifyadmin")
    suspend fun verifyAdmin(
        @Field("secret_text") secretText: String?,
    ): VerifyAdminResponse


}