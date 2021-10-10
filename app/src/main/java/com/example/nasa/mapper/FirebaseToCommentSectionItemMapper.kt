package com.example.nasa.mapper

import com.example.domain.models.firebase.Comment
import com.example.nasa.adapter.commentsection.CommentItem
import com.example.nasa.adapter.commentsection.CommentsSectionItem

fun Comment.asCommentsSectionItem(): CommentItem{
    return CommentItem(senderNickname = this.senderNickname ?: "",
        dateWritten = dateWritten?: "", text = text?: "")
}

fun List<Comment>.asCommentsSectionItem(): List<CommentItem>{
    return this.map{it.asCommentsSectionItem()}
}