package com.example.domain.repositories

import com.example.domain.models.firebase.NASAPost
import com.example.domain.models.firebase.NASAPostCommentsModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface FirebasePostRepository {

    fun addPostToLiked(nasaPost: NASAPost): Completable

    fun deletePostFromLiked(nasaId: String): Completable

    fun getLikedStatus(nasaId: String): Single<Boolean>

    fun getComments(nasaId: String): Observable<NASAPostCommentsModel>

    fun postComment(commentId: String, nasaId: String, comment: String): Completable
}