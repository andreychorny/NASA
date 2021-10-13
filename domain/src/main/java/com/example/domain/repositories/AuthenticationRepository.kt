package com.example.domain.repositories

import io.reactivex.Completable

interface AuthenticationRepository {

    fun signUpUser(nickname: String, email: String, password: String): Completable

    fun signInUser(email: String, password: String): Completable
}