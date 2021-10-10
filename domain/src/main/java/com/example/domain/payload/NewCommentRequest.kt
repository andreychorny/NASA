package com.example.domain.payload

data class NewCommentRequest (
    val commentId: String,
    val nasaId: String,
    val commentText: String
)