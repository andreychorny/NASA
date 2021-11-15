package com.example.nasa.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.backend.NASAImageModel
import com.example.domain.models.firebase.NASAPostCommentsModel
import com.example.domain.payload.NewCommentRequest
import com.example.domain.usecases.backend.GetSingleNasaItemUseCase
import com.example.domain.usecases.firebase.*
import com.example.domain.usecases.firebase.comment.LoadCommentsUseCase
import com.example.domain.usecases.firebase.comment.PostNewCommentUseCase
import com.example.nasa.adapter.commentsection.CommentInputItem
import com.example.nasa.adapter.commentsection.CommentsSectionItem
import com.example.nasa.adapter.commentsection.NoSignedUserAlertItem
import com.example.nasa.mapper.asCommentsSectionItem
import com.example.nasa.rx.SchedulersProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class NASADetailsViewModel @Inject constructor(
    private val loadCommentsUseCase: LoadCommentsUseCase,
    private val postNewCommentUseCase: PostNewCommentUseCase,
    private val getSingleNasaItemUseCase: GetSingleNasaItemUseCase,
    private val addPostToLikedUseCase: AddPostToLikedUseCase,
    private val deletePostFromLikedUseCase: DeletePostFromLikedUseCase,
    private val loadPostLikedStatusUseCase: LoadPostLikedStatusUseCase,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private var nasaPostToCommentsModel: NASAPostCommentsModel? = null

    private val nasaComments = MutableLiveData<List<CommentsSectionItem>>()
    fun nasaComments(): LiveData<List<CommentsSectionItem>> = nasaComments

    private val nasaModel = MutableLiveData<NASAImageModel>()
    fun nasaModel(): LiveData<NASAImageModel> = nasaModel

    private val isPostLiked = MutableLiveData<Boolean?>()
    fun isPostLiked(): LiveData<Boolean?> = isPostLiked

    private val disposables = CompositeDisposable()

    private var likeStatusCheckDisposable: Disposable? = null

    fun loadNasaPost(nasaId: String) {
        disposables.add(
            getSingleNasaItemUseCase.execute(nasaId)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe({
                    nasaModel.value = it
                }, {
                    throw IllegalArgumentException("No such nasa id found")
                })
        )
    }

    fun loadIsLikedState(nasaId: String){
        if(Firebase.auth.currentUser != null){
            disposables.add(loadPostLikedStatusUseCase.execute(nasaId)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .subscribe({
                    isPostLiked.value = it
                },{
                    Log.e(TAG, "Unable to load liked status")
                }))
        }else {
            isPostLiked.value = null
        }
    }

    fun setNasaModel(model: NASAImageModel) {
        nasaModel.value = model
        loadIsLikedState(model.nasaId)
    }

    fun loadComments(nasaId: String) {
        if (Firebase.auth.currentUser != null) {
            nasaComments.value = listOf(CommentInputItem)
        } else {
            nasaComments.value = listOf(NoSignedUserAlertItem)
        }
        disposables.add(
            loadCommentsUseCase.execute(nasaId)
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

    fun postComment(comment: String) {
        val commentId = if (nasaPostToCommentsModel == null) {
            0
        } else {
            nasaPostToCommentsModel!!.comments?.size ?: 0
        }
        nasaModel.value?.nasaId?.let { nasaId ->
            val postCommentRequest =
                NewCommentRequest(commentId.toString(), nasaId, comment)
            disposables.add(
                postNewCommentUseCase.execute(postCommentRequest)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .subscribe()
            )
        }
    }

    fun cancelAllDisposables() {
        disposables.dispose()
        likeStatusCheckDisposable?.dispose()
    }

    fun executeLikeButton() {
        nasaModel.value?.let {
            if(isPostLiked.value == false) {
                addToLiked(it)
            }else{
                removeFromLiked(it.nasaId)
            }
        }
    }

    private fun addToLiked(model: NASAImageModel){
        likeStatusCheckDisposable?.dispose()
        likeStatusCheckDisposable = addPostToLikedUseCase.execute(model)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                isPostLiked.value = true
            }, {

            })
    }

    private fun removeFromLiked(id: String){
        likeStatusCheckDisposable?.dispose()
        likeStatusCheckDisposable = deletePostFromLikedUseCase.execute(id)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .subscribe({
                isPostLiked.value = false
            }, {

            })
    }
}
