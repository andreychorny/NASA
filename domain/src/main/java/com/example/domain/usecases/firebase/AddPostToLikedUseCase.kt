package com.example.domain.usecases.firebase

import com.example.domain.models.backend.NASAImageModel
import com.example.domain.models.firebase.NASAPost
import com.example.domain.repositories.FirebasePostRepository
import com.example.domain.usecases.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class AddPostToLikedUseCase @Inject constructor(private val repo: FirebasePostRepository):
    CompletableUseCase<NASAImageModel> {

    override fun execute(input: NASAImageModel): Completable {
        return repo.addPostToLiked(NASAPost(
            nasaId = input.nasaId,
            title = input.title,
            imgUrl = input.imageUrl))
    }
}