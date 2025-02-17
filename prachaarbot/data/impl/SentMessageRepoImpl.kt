package com.prachaarbot.data.impl

import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.GetMessageBodyResponse
import com.prachaarbot.data.model.SentMessageResponse
import com.prachaarbot.data.remote.Response
import com.prachaarbot.data.remote.SentMessageApi
import com.prachaarbot.domain.repo.SentMessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class SentMessageRepoImpl @Inject constructor(
    private val sentMessageApi: SentMessageApi,
    private val localPrefData: LocalPrefData
) :
    SentMessageRepository {


    override fun messageSentStatus(
        platform: String,
        mobile: String,
        status: String,
        messageBody: String,
    ): Flow<Response<SentMessageResponse>> = flow {

        val result = sentMessageApi.sentMessage(
            localPrefData.dataTypeSelected,
            messageBody,
            localPrefData.user?.id.toString(),
            platform,
            mobile,
            status,
            localPrefData.boothId
        )

        if (result != null) {
            emit(Response.success(result))
        } else {
            emit(Response.error("Failed to upload message sent status"))
        }
    }.catch {

        emit((Response.error("Failed to upload message sent status")))
    }

    override fun getSmsData(): Flow<Response<GetMessageBodyResponse>> = flow {

        val result = sentMessageApi.getSmsBody(localPrefData.user?.id?.toString()?:"", if(localPrefData.boothId.isNullOrBlank().not()) localPrefData.boothId!! else "0" )
        if (result != null) {
            emit(Response.success(result))
        } else {
            emit(Response.error("Failed to fetch message body"))
        }
    }.catch {
        emit(Response.error("Failed to fetch message body"))
    }
}

