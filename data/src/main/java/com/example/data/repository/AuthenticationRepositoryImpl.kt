package com.example.data.repository

import android.util.Log
import com.example.domain.models.firebase.User
import com.example.domain.repositories.AuthenticationRepository
import com.example.domain.repositories.FirebaseCommentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val database: DatabaseReference
) : AuthenticationRepository {

    override fun signUpUser(nickname: String, email: String, password: String): Completable =
        Completable.create { subscriber ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = User(nickname)
                        auth.currentUser?.let {
                            createUserEntryInDatabase(it, user, subscriber)
                        }
                        val profile = userProfileChangeRequest {
                            displayName = nickname
                        }
                        auth.currentUser?.updateProfile(profile)
                        subscriber.onComplete()
                    } else {
                        subscriber.onError(
                            Exception(
                                task.exception?.message ?: "Error on account sign-up"
                            )
                        )
                        Log.e(
                            "AuthenticationRepo",
                            task.exception?.message ?: "Error on account sign-up"
                        )
                    }
                }
        }

    override fun signInUser(email: String, password: String): Completable =
        Completable.create { subscriber ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        subscriber.onComplete()
                    } else {
                        subscriber.onError(
                            Exception(
                                task.exception?.message ?: "Authentication failed."
                            )
                        )
                        Log.e(
                            "AuthenticationFragment",
                            task.exception?.message ?: "Authentication failed."
                        )
                    }
                }
        }

    private fun createUserEntryInDatabase(
        it: FirebaseUser,
        user: User,
        subscriber: CompletableEmitter
    ) = database.child("users")
        .child(it.uid)
        .setValue(user).addOnCompleteListener {
            if (!it.isSuccessful) {
                subscriber.onError(Exception(it.exception?.message ?: "Error on account sign-up"))
                Log.e("AuthenticationRepo", it.exception?.message ?: "")
            }
        }
}