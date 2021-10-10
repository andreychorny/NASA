package com.example.domain.repositories

import com.example.domain.models.firebase.NASAPostCommentsModel
import io.reactivex.Completable
import io.reactivex.Observable

interface FirebaseCommentRepository {

    fun getComments(nasaId: String): Observable<NASAPostCommentsModel>

    fun postComments(commentId: String, nasaId: String, comment: String): Completable
}