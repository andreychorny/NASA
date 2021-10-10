package com.example.domain.repositories

import android.graphics.Bitmap
import com.example.domain.models.firebase.User
import io.reactivex.Single

interface FirebaseUserRepository {

    fun getCurrentUserByUid(uid: String): Single<User>

    fun uploadProfilePicture(bitmap: Bitmap): Single<Bitmap>
}