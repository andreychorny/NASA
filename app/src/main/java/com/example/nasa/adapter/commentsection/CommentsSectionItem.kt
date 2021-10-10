package com.example.nasa.adapter.commentsection


sealed class CommentsSectionItem

data class CommentItem(
    val senderNickname: String,
    val dateWritten: String,
    val text: String
): CommentsSectionItem()

object CommentInputItem: CommentsSectionItem()

object NoSignedUserAlertItem: CommentsSectionItem()