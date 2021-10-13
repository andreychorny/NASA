package com.example.domain.usecases.firebase.authentication

import com.example.domain.payload.SignInRequest
import com.example.domain.repositories.AuthenticationRepository
import com.example.domain.usecases.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class SignInUserUseCase @Inject constructor(private val repo: AuthenticationRepository):
    CompletableUseCase<SignInRequest> {

    override fun execute(input: SignInRequest): Completable {
        return  repo.signInUser(
            email = input.email,
            password = input.password
        )
    }
}