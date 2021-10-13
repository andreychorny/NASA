package com.example.nasa.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.firebase.Comment
import com.example.domain.models.firebase.NASAPostCommentsModel
import com.example.domain.payload.NewCommentRequest
import com.example.domain.usecases.firebase.LoadCommentsUseCase
import com.example.domain.usecases.firebase.PostNewCommentUseCase
import com.example.nasa.adapter.commentsection.CommentInputItem
import com.example.nasa.adapter.commentsection.CommentsSectionItem
import com.example.nasa.adapter.commentsection.NoSignedUserAlertItem
import com.example.nasa.mapper.asCommentsSectionItem
import com.example.nasa.rx.SchedulersProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NASADetailsViewModel @Inject constructor(
    private val loadCommentsUseCase: LoadCommentsUseCase,
    private val postNewCommentUseCase: PostNewCommentUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private var nasaPostToCommentsModel: NASAPostCommentsModel? = null

    private val nasaComments = MutableLiveData<List<CommentsSectionItem>>()
    fun nasaComments(): LiveData<List<CommentsSectionItem>> = nasaComments

    private val disposables = mutableListOf<Disposable>()

    fun loadComments(nasaId: String) {
        if (Firebase.auth.currentUser != null) {
            nasaComments.value = listOf(CommentInputItem)
        } else {
            nasaComments.value = listOf(NoSignedUserAlertItem)
        }
        disposables.add(loadCommentsUseCase.execute(nasaId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                val resultList = mutableListOf<CommentsSectionItem>()
                if (Firebase.auth.currentUser != null) {
                    resultList.add(CommentInputItem)
                } else {
                    resultList.add(NoSignedUserAlertItem)
                }
                nasaPostToCommentsModel = it
                resultList.addAll(
                    nasaPostToCommentsModel?.comments?.asCommentsSectionItem() ?: emptyList()
                )
                nasaComments.value = resultList
            }, {
                Log.e(TAG, it.message ?: "Loading of comments was unsuccessful")
            })
        )
    }

    fun postComment(comment: String, nasaId: String) {
        val commentId = if (nasaPostToCommentsModel == null) {
            0
        } else {
            nasaPostToCommentsModel!!.comments?.size ?: 0
        }
        val postCommentRequest = NewCommentRequest(commentId.toString(), nasaId, comment)
        postNewCommentUseCase.execute(postCommentRequest)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe()
    }

    fun cancelAllDisposables() {
        disposables.forEach { it.dispose() }
    }
}
