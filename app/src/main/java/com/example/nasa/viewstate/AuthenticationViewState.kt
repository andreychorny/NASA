package com.example.nasa.viewstate

sealed class AuthenticationViewState {

    object Loading: AuthenticationViewState()

    object Success: AuthenticationViewState()

    data class Error(val message: String): AuthenticationViewState()

}