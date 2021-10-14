package com.example.domain.usecases.firebase

import com.example.domain.payload.NewCommentRequest
import com.example.domain.repositories.FirebasePostRepository
import com.example.domain.usecases.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class PostNewCommentUseCase @Inject constructor(private val repo: FirebasePostRepository):
    CompletableUseCase<NewCommentRequest>  {

    override fun execute(input: NewCommentRequest): Completable {
        return repo.postComment(input.commentId, input.nasaId, input.commentText)
    }

}