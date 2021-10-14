package com.example.domain.models.firebase

data class UserToLikedPosts(
    val nickname: String,
    val posts: List<NASAPost>
)