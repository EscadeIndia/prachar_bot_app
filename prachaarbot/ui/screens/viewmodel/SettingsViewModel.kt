package com.prachaarbot.ui.screens.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.LogOutResponse
import com.prachaarbot.domain.use_case.LoginUseCase
import com.prachaarbot.ui.state.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
internal class SettingsViewModel
@Inject constructor(
    private val localSharedPref: LocalPrefData,
    @ApplicationContext private val context: Context,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _userState =
        MutableSharedFlow<ViewState<LogOutResponse>>()
    val userState = _userState.asSharedFlow()

    var defaultSim = localSharedPref.defaultSimSelected
    var messageLimitCount = localSharedPref.messageLimitCount
    var textEnabled = localSharedPref.isTextMessageEnabled
    var whatsAppEnabled = localSharedPref.isWhatsAppMessageEnabled
    var whatsAppWaitingTime = localSharedPref.whatsAppWaitingTime
    var whatsAppBulkWaitingTime = localSharedPref.whatsAppBulkWaitingTime

    private val _savedDataState =
        MutableSharedFlow<ViewState<String>>()
    val savedDataState = _savedDataState.asSharedFlow()
    fun saveData() {
        localSharedPref.defaultSimSelected = defaultSim
        localSharedPref.messageLimitCount = messageLimitCount
        localSharedPref.isTextMessageEnabled = textEnabled
        localSharedPref.isWhatsAppMessageEnabled = whatsAppEnabled
        localSharedPref.whatsAppWaitingTime = whatsAppWaitingTime
        localSharedPref.whatsAppBulkWaitingTime = whatsAppBulkWaitingTime
        viewModelScope.launch {
            _savedDataState.emit(ViewState.success("Settings Saved"))
        }
    }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _userState.emit(ViewState.loading())
            loginUseCase.logOutUser().distinctUntilChanged().collect {
                _userState.emit(ViewState.success(LogOutResponse()))
                localSharedPref.clearSharedPrefData()
            }
        }
    }

    fun getBlockedContactsSize() = localSharedPref.blockedContacts?.size ?: 0
}