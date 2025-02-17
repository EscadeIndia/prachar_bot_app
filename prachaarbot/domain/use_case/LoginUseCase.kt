package com.prachaarbot.domain.use_case

import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.UserData
import com.prachaarbot.domain.repo.LoginRepository
import com.prachaarbot.ui.state.LoginState
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val loginRepo: LoginRepository,
    private val localPrefData: LocalPrefData
) {

    fun checkForUserLoginState(): LoginState {
        return if (localPrefData.user != null && localPrefData.isUserLoggedIn) {
            LoginState.LoggedIn
        } else {
            LoginState.NotLoggedIn
        }
    }


    fun checkCredentialsValid(
        username: String,
        password: String,
        isPhoneNumber: Boolean = false
    ): Pair<Boolean, String> {
        val key = if (isPhoneNumber) "Phone Number" else "Password"
        return if (username.isNullOrBlank())
            Pair(false, "Username cant be empty")
        else if (password.isNullOrBlank())
            Pair(false, "$key cant be empty")
        else Pair(true, "")
    }

    suspend fun loginUser(username: String, password: String) =
        loginRepo.login(username, password)


    fun updatePrefData(data: UserData?) {
        if (data?.token.isNullOrBlank().not()) {
            localPrefData.authToken = data?.token!!
        }
        data?.user?.let {
            localPrefData.user = it
        }
        localPrefData.isUserLoggedIn = true
    }

    suspend fun logOutUser() = loginRepo.logout()

    suspend fun addBoothMember(name: String, number: String) =
        loginRepo.addMember(name, number, localPrefData.user?.id?.toString() ?: "")

    fun updateBoothId(id: Int?) {
        if (id?.toString().isNullOrBlank().not()) {
            localPrefData.boothId = id?.toString()
        }
    }
//    suspend fun verifyAdmin(secretText: String) = loginRepo.verifyAdmin(secretText).also {
//        it.collectLatest {
//            if(it is Response.Success) {
//                    localPrefData.isUserOwner = true
//            }
//        }
//    }

    fun verifyAdmin(secretText: String) =
        localPrefData.user?.secretText.equals(secretText, true).also {
            if (it) {
                localPrefData.isUserOwner = true
            }
        }

}