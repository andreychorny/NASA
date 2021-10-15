package com.example.domain.usecases.firebase

import com.example.domain.models.firebase.User
import com.example.domain.models.firebase.UserActivities
import com.example.domain.repositories.FirebaseUserRepository
import com.example.domain.usecases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetUserActivitiesUseCase @Inject constructor(private val repo: FirebaseUserRepository):
    SingleUseCase<UserActivities, String> {

    // input is user nickname
    override fun execute(input: String): Single<UserActivities> {
        return repo.getUserActivities(input)
    }
}