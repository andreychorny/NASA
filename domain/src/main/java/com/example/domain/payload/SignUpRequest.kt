package com.example.domain.payload

data class SignUpRequest(
    val nickname: String,
    val email: String,
    val password: String
)