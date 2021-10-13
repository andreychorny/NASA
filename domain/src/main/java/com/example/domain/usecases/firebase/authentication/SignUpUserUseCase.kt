package com.example.domain.usecases.firebase.authentication

import com.example.domain.payload.SignUpRequest
import com.example.domain.repositories.AuthenticationRepository
import com.example.domain.usecases.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(private val repo: AuthenticationRepository):
    CompletableUseCase<SignUpRequest> {

    override fun execute(input: SignUpRequest): Completable {
        return  repo.signUpUser(
            nickname = input.nickname,
            email = input.email,
            password = input.password
        )
    }
}