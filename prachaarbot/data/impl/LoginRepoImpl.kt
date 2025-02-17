package com.prachaarbot.data.impl

import com.prachaarbot.data.model.LogOutResponse
import com.prachaarbot.data.model.AddBoothMemberResponse
import com.prachaarbot.data.model.UserResponse
import com.prachaarbot.data.remote.LoginApi
import com.prachaarbot.data.remote.Response
import com.prachaarbot.domain.repo.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class LoginRepoImpl @Inject constructor(
    private val loginApi: LoginApi,
) :
    LoginRepository {
    override fun login(userName: String, password: String): Flow<Response<UserResponse>> =
        flow {

            val result = loginApi.login(userName, password)
            if (result != null) {
                emit(Response.success(result))
            } else {
                emit(Response.error("Failed to login. Retry"))
            }
        }.catch {
            emit((Response.error("Failed to login. Retry later")))
        }

    override fun logout(): Flow<Response<LogOutResponse>> = flow {
        val result = loginApi.logOut()
        if (result != null) {
            emit(Response.success(result))
        } else {
            emit(Response.error("Couldn't log out user. Please try again"))
        }
    }.catch {
        emit((Response.error("Couldn't log out user. Please try later")))
    }

    override fun addMember(name: String, number: String, id: String): Flow<Response<AddBoothMemberResponse>> =  flow {
        val result = loginApi.addMember(name, number, id)
        if (result != null) {
            emit(Response.success(result))
        } else {
            emit(Response.error("Failed to set user profile. Please try again"))
        }
    }.catch {
        emit(Response.error(it.message.toString()))
    }

//    override fun verifyAdmin(secretText: String): Flow<Response<VerifyAdminResponse>> =  flow {
//        val result = loginApi.verifyAdmin(secretText)
//        if (result != null) {
//            emit(Response.success(result))
//        } else {
//            emit(Response.error("Failed to set user profile. Please try again"))
//        }
//    }.catch {
//        emit(Response.error(it.message.toString()))
//    }
}

