package com.example.nasa.viewstate

import com.example.domain.models.NASAImageModel

sealed class AuthenticationViewState {

    object Loading: AuthenticationViewState()

    object Success: AuthenticationViewState()

    data class Error(val message: String): AuthenticationViewState()

}