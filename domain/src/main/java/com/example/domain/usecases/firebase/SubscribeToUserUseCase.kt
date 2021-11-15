package com.example.domain.usecases.firebase

import com.example.domain.repositories.FirebaseUserRepository
import com.example.domain.usecases.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class SubscribeToUserUseCase @Inject constructor(private val repo: FirebaseUserRepository):
    CompletableUseCase<String> {

    override fun execute(input: String): Completable {
        return repo.subscribeToUser(input)
    }

}