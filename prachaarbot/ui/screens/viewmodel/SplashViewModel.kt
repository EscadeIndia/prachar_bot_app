package com.prachaarbot.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.domain.use_case.LoginUseCase
import com.prachaarbot.ui.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val localPrefData: LocalPrefData
) :
    ViewModel() {

    private val _loginStatus =
        MutableStateFlow<LoginState>(LoginState.Loading)
    val loginStatus = _loginStatus.asStateFlow()

    fun checkForLoginState() {
        _loginStatus.value = loginUseCase.checkForUserLoginState()
    }

    fun usersProfileTypeIsSet(): Boolean {
        return (localPrefData.isUserOwner || localPrefData.boothId.isNullOrBlank().not())
    }

}