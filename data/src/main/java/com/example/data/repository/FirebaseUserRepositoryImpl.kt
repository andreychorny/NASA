package com.example.data.repository

import android.graphics.Bitmap
import com.example.domain.models.firebase.User
import com.example.domain.models.firebase.UserActivities
import com.example.domain.repositories.FirebaseUserRepository
import com.example.domain.repositories.RemoteSearchRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference
import io.reactivex.Single
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val storage: StorageReference,
    val database: DatabaseReference
) : FirebaseUserRepository {

    override fun getCurrentUserByUid(uid: String): Single<User> = Single.create { subscriber ->
        database.child("users").child(uid).get().addOnSuccessListener {
            it.getValue<User>()?.let { user -> subscriber.onSuccess(user) }
        }
    }

    override fun getUserActivities(nickname: String): Single<UserActivities> = Single.create{ subscriber ->
        database.child("user-activities").child(nickname).get().addOnSuccessListener {
            it.getValue<UserActivities>()?.let { user -> subscriber.onSuccess(user) }
        }
    }

    override fun uploadProfilePicture(bitmap: Bitmap): Single<Bitmap> =
        Single.create { subscriber ->
            val userImageRef = storage
                .child("users/${auth.currentUser?.displayName}.jpg")
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = userImageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                subscriber.onError(it)
            }.addOnSuccessListener {
                subscriber.onSuccess(bitmap)
            }
        }

}
