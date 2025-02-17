package com.prachaarbot.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prachaarbot.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MessageStatusViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) :
    ViewModel() {


    fun messageSent(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.loginUser(username, password)
        }
    }


}