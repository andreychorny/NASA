package com.example.domain.usecases.firebase

import com.example.domain.repositories.FirebasePostRepository
import com.example.domain.usecases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class LoadPostLikedStatusUseCase @Inject constructor(private val repo: FirebasePostRepository):
    SingleUseCase<Boolean, String> {

    //input is nasaId of the post
    override fun execute(input: String): Single<Boolean> {
        return repo.getLikedStatus(input)
    }
}