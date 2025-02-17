package com.prachaarbot.domain.repo

import com.prachaarbot.data.model.LogOutResponse
import com.prachaarbot.data.model.AddBoothMemberResponse
import com.prachaarbot.data.model.UserResponse
import com.prachaarbot.data.remote.Response
import kotlinx.coroutines.flow.Flow


interface LoginRepository {
    fun login(userName: String, password: String): Flow<Response<UserResponse>>

    fun logout(): Flow<Response<LogOutResponse>>

    fun addMember(name: String, number: String, id: String): Flow<Response<AddBoothMemberResponse>>

//    fun verifyAdmin(secretText: String) : Flow<Response<VerifyAdminResponse>>
}

