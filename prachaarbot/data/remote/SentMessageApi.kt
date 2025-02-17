package com.prachaarbot.data.remote

import com.prachaarbot.data.model.GetMessageBodyResponse
import com.prachaarbot.data.model.SentMessageResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


internal interface  SentMessageApi {

    @FormUrlEncoded
    @POST("sendsms")
    suspend fun sentMessage(
        @Field("datatype") datatype: String?,
        @Field("datavalue") dataValue: String?,
        @Field("user_id") userId: String?,
        @Field("platform") platform: String?,
        @Field("mobile") mobile: String?,
        @Field("status") status: String?,
        @Field("boothmember_id") boothMemberId: String?
        ): SentMessageResponse

    @GET("getsms/{id}/{boothmember_id}")
    suspend fun getSmsBody(@Path("id") id: String, @Path("boothmember_id") boothMemberId: String) : GetMessageBodyResponse
}