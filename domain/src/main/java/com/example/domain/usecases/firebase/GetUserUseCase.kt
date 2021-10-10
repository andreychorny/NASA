package com.example.domain.usecases.firebase

import com.example.domain.models.firebase.User
import com.example.domain.repositories.FirebaseUserRepository
import com.example.domain.usecases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repo: FirebaseUserRepository):
    SingleUseCase<User, String> {

    override fun execute(input: String): Single<User> {
        return  repo.getCurrentUserByUid(input)
    }

}