package com.example.data.repository

import android.content.ContentValues
import android.util.Log
import com.example.domain.models.firebase.Comment
import com.example.domain.models.firebase.NASAPostCommentsModel
import com.example.domain.repositories.FirebaseCommentRepository
import com.example.domain.repositories.FirebaseUserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import io.reactivex.Completable
import io.reactivex.Observable
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class FirebaseCommentRepositoryImpl @Inject constructor(
    val auth: FirebaseAuth,
    val database: DatabaseReference
) : FirebaseCommentRepository {

    //TODO consider retrieving Bitmap pic together with comment info
    override fun getComments(nasaId: String): Observable<NASAPostCommentsModel> =
        Observable.create { subscriber ->
            val nasaPostReference = database.child("nasaPostToComments").child(nasaId)
            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val nasaPostToCommentsModel = dataSnapshot.getValue<NASAPostCommentsModel>()
                    if (nasaPostToCommentsModel != null) {
                        subscriber.onNext(nasaPostToCommentsModel)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                    subscriber.onComplete()
                }
            }
            nasaPostReference.addValueEventListener(postListener)
        }

    override fun postComments(commentId: String, nasaId: String, commentText: String): Completable =
        Completable.create { subscriber ->
            if (auth.currentUser?.displayName != null) {
                val df = DateFormat.getTimeInstance()
                df.timeZone = TimeZone.getTimeZone("gmt")
                val gmtTime = df.format(Date())

                val newComment = Comment(
                    senderNickname = Firebase.auth.currentUser!!.displayName!!,
                    dateWritten = gmtTime,
                    text = commentText
                )
                val nasaCommentsReference = database.child("nasaPostToComments")
                    .child(nasaId).child("comments")
                    .child(commentId)
                nasaCommentsReference.setValue(newComment).addOnSuccessListener {
                    subscriber.onComplete()
                }.addOnFailureListener {
                    subscriber.onError(it)
                }
            } else {
                subscriber.onError(Exception("User is not logged in to leave comments"))
            }
        }
//        Way to store downloadUrl in User if needed after pushing img
//        ref.putBytes(bytes).addOnCompleteListener { task1 ->
//            if (task1.isSuccessful) {
//                ref.downloadUrl.addOnCompleteListener { task2 ->
//                    if (task2.isSuccessful) {
//
//                    }
//                }

}
