package com.example.domain.usecases.firebase

import com.example.domain.repositories.FirebasePostRepository
import com.example.domain.usecases.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class DeletePostFromLikedUseCase @Inject constructor(private val repo: FirebasePostRepository):
    CompletableUseCase<String> {

    //Input is nasaId of post to delete
    override fun execute(input: String): Completable {
        return repo.deletePostFromLiked(input)
    }
}