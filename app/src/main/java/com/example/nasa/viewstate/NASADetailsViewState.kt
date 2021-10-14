package com.example.nasa.viewstate

import com.example.domain.models.backend.NASAImageModel
import com.example.nasa.adapter.commentsection.CommentsSectionItem

sealed class NASADetailsViewState

    object Loading : NASADetailsViewState()

    data class PostIsLoaded(val nasaPost: NASAImageModel): NASADetailsViewState()

    data class CommentsAreLoaded(val commentsList: List<CommentsSectionItem>): NASADetailsViewState()

    object PostingAComment: NASADetailsViewState()

    object CommentIsPosted: NASADetailsViewState()

    data class Error(val message: String): NASADetailsViewState()