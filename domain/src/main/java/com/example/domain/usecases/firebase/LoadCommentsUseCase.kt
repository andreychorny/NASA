package com.example.domain.usecases.firebase

import com.example.domain.models.firebase.NASAPostCommentsModel
import com.example.domain.repositories.FirebaseCommentRepository
import com.example.domain.usecases.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject

class LoadCommentsUseCase @Inject constructor(private val repo: FirebaseCommentRepository):
    ObservableUseCase<NASAPostCommentsModel, String> {

    //input - nasa post id
    override fun execute(input: String): Observable<NASAPostCommentsModel> {
        return repo.getComments(input)
    }
}