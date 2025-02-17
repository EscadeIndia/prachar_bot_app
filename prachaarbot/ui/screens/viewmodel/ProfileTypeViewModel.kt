package com.prachaarbot.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.AddBoothMemberResponse
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
internal class ProfileTypeViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) :
    ViewModel() {

    private val _userprofileBoothWorkerState =
        MutableStateFlow<ViewState<AddBoothMemberResponse>>(ViewState.Initial())
    val userprofileBoothWorkerState = _userprofileBoothWorkerState.asStateFlow()

    private val _userprofileAdminState =
        MutableStateFlow<Boolean?>(null)
    val userprofileAdminState = _userprofileAdminState.asStateFlow()

    fun setUserProfileToBoothWorker(name: String, number: String) {
        _userprofileBoothWorkerState.value = ViewState.loading()
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.checkCredentialsValid(name, number, true).let {
                if (it.first) {
                    loginUseCase.addBoothMember(name, number).distinctUntilChanged().collect {
                        when (it) {
                            is Response.Error -> _userprofileBoothWorkerState.value =
                                ViewState.error(it.message)

                            is Response.Success -> {
                                loginUseCase.updateBoothId(it.data.data?.id)
                                _userprofileBoothWorkerState.value = ViewState.success(it.data)
                            }
                        }
                    }
                } else {
                    _userprofileBoothWorkerState.value = ViewState.error(it.second)
                }
            }
        }
    }

    fun checkForOwnerVerification(secretText: String) {
        _userprofileAdminState.value = loginUseCase.verifyAdmin(secretText)


    }
}