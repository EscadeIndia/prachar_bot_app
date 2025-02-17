package com.prachaarbot.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.UserResponse
import com.prachaarbot.data.remote.Response
import com.prachaarbot.domain.use_case.LoginUseCase
import com.prachaarbot.ui.state.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val localPrefData: LocalPrefData
) :
    ViewModel() {


    private val _userState =
        MutableStateFlow<ViewState<UserResponse>>(ViewState.Initial())
    val userState = _userState.asStateFlow()

    fun loginUser(username: String, password: String) {
        _userState.value = ViewState.loading()
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.checkCredentialsValid(username, password).let {
                if (it.first) {
                    loginUseCase.loginUser(username, password).distinctUntilChanged().collect {
                        when (it) {
                            is Response.Error -> _userState.value = ViewState.error(it.message)
                            is Response.Success -> {
                                loginUseCase.updatePrefData(it.data.data)
                                _userState.value = ViewState.success(it.data)
                            }

                        }
                    }
                } else {
                    _userState.value = ViewState.error(it.second)
                }
            }
        }
    }

    fun usersProfileTypeIsSet(): Boolean {
        return (localPrefData.isUserOwner || localPrefData.boothId.isNullOrBlank().not())
    }

    fun getPrivacyDisclosureShown() = localPrefData.privacyDisclosureShown
    fun updatePrivacyDisclosureShown() {
        localPrefData.privacyDisclosureShown = true
    }

}