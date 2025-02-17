package com.prachaarbot.ui.state

sealed class ViewState<T> {
    class Initial<T>() : ViewState<T>()
    class Loading<T>() : ViewState<T>()
    class Success<T>(val data: T) : ViewState<T>()
    class Error<T>(val message: String) : ViewState<T>()

    companion object {
        fun <T> initial() = Initial<T>()
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success<T>(data)
        fun <T> error(message: String) = Error<T>(message)
    }
}