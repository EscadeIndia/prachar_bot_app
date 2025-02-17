package com.prachaarbot.ui.screens.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import com.prachaarbot.data.model.GetMessageBodyResponse
import com.prachaarbot.data.remote.Response
import com.prachaarbot.domain.use_case.SentMessageUseCase
import com.prachaarbot.ui.state.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val localPrefData: LocalPrefData,
    @ApplicationContext private val context: Context,
    private val sentMessageUseCase: SentMessageUseCase
) : ViewModel() {

    // ViewState for message body data
    private val _messageBodyState =
        MutableStateFlow<ViewState<GetMessageBodyResponse>>(ViewState.Initial())
    val messageBodyState = _messageBodyState.asStateFlow()

    // ViewState for opening a URL intent
    private val _openUrlIntentState =
        MutableStateFlow<ViewState<Boolean>>(ViewState.Initial())
    val openUrlIntentState = _openUrlIntentState.asStateFlow()

    init {
        // Collect SMS body data from sentMessageUseCase in a background thread
        viewModelScope.launch(Dispatchers.IO) {
            sentMessageUseCase.getSmsBody().distinctUntilChanged().collect { response ->
                when (response) {
                    is Response.Error -> {
                        // Handle error state and update the message body state
                        _messageBodyState.value = ViewState.Error(response.message)
                    }
                    is Response.Success -> {
                        // Update the preferences with the fetched data and set success state
                        sentMessageUseCase.updatePref(response.data.data)
                        _messageBodyState.value = ViewState.Success(response.data)
                    }
                }
            }
        }
    }

    // Function to get the user data (for example, user preferences)
    fun getUserData() = localPrefData.user

    // Function to get the current application state (active/inactive)
    fun getApplicationState() = localPrefData.isAppActive

    // Function to update the application state (active/inactive)
    fun updateApplicationActiveState(flag: Boolean) {
        localPrefData.isAppActive = flag
    }

    // Function to open the user's profile URL in a browser (if valid)
    fun openUserProfileIntent() {
        val profileUrl = localPrefData.user?.profileUrl
        if (!profileUrl.isNullOrBlank()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(profileUrl))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } catch (e: Exception) {
                // Handle the error if the URL is invalid
                _openUrlIntentState.value = ViewState.Error("Invalid URL")
            }
        } else {
            // Handle the case where the profile URL is blank or null
            _openUrlIntentState.value = ViewState.Error("Invalid URL")
        }
    }
}

