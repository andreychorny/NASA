package com.example.data.repository

import android.content.ContentValues
import android.graphics.Bitmap
import android.util.Log
import com.example.domain.models.firebase.NASAPostCommentsModel
import com.example.domain.models.firebase.User
import com.example.domain.models.firebase.UserActivities
import com.example.domain.repositories.FirebaseUserRepository
import com.example.domain.repositories.RemoteSearchRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.io.ByteArrayOutputStream
import java.lang.Exception
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

    override fun getUserActivities(nickname: String): Observable<UserActivities> =
        Observable.create { subscriber ->
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userActivities = dataSnapshot.getValue<UserActivities>()
                    if (userActivities != null) {
                        subscriber.onNext(userActivities)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                    subscriber.onComplete()
                }
            }
            val ref = database.child("user-activities").child(nickname)
            ref.addValueEventListener(postListener)
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

    override fun subscribeToUser(nickname: String): Completable = Completable.create { subscriber ->
        val currentUserNickname = auth.currentUser?.displayName
        if (currentUserNickname != null) {
            database.child("user-activities").child(currentUserNickname)
                .child("subscriptions").child(nickname).setValue(nickname)
            addSubscriberToUser(nickname, currentUserNickname)
            subscriber.onComplete()
        } else {
            subscriber.onError(Exception("No authenticated user"))
        }
    }

    override fun unsubscribeFromUser(nickname: String): Completable =
        Completable.create { subscriber ->
            val currentUserNickname = auth.currentUser?.displayName
            if (currentUserNickname != null) {
                database.child("user-activities").child(currentUserNickname)
                    .child("subscriptions").child(nickname).removeValue()
                removeSubscriberFromUser(nickname, currentUserNickname)
                subscriber.onComplete()
            } else {
                subscriber.onError(Exception("No authenticated user"))
            }
        }

    private fun addSubscriberToUser(
        nicknameOfSubscription: String,
        nicknameOfSubscriber: String
    ) {
        database.child("user-activities").child(nicknameOfSubscription)
            .child("subscribers").child(nicknameOfSubscriber).setValue(nicknameOfSubscriber)
    }

    private fun removeSubscriberFromUser(
        nicknameOfSubscription: String,
        nicknameOfSubscriber: String
    ) {
        database.child("user-activities").child(nicknameOfSubscription)
            .child("subscribers").child(nicknameOfSubscriber).removeValue()
    }
}
