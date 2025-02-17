package com.prachaarbot.domain.repo

import com.prachaarbot.data.model.GetMessageBodyResponse
import com.prachaarbot.data.model.SentMessageResponse
import com.prachaarbot.data.remote.Response
import kotlinx.coroutines.flow.Flow


interface SentMessageRepository {
    fun messageSentStatus(platform: String, mobile: String, status: String, messageBody: String): Flow<Response<SentMessageResponse>>

    fun getSmsData() : Flow<Response<GetMessageBodyResponse>>
}

