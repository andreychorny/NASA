package com.example.domain.models.firebase

data class NASAPostCommentsModel(
    val nasaId: String? = null,
    val comments: MutableList<Comment>? = null
)