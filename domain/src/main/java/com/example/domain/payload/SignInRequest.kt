package com.example.domain.payload

data class SignInRequest(
    val email: String,
    val password: String
)