package com.example.domain.repositories

import android.graphics.Bitmap
import com.example.domain.models.firebase.User
import com.example.domain.models.firebase.UserActivities
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface FirebaseUserRepository {

    fun getCurrentUserByUid(uid: String): Single<User>

    fun getUserActivities(nickname: String): Observable<UserActivities>

    fun uploadProfilePicture(bitmap: Bitmap): Single<Bitmap>

    fun subscribeToUser(nickname: String): Completable

    fun unsubscribeFromUser(nickname: String): Completable
}