package com.example.domain.models.firebase

data class UserActivities(
    val likedPosts: HashMap<String, NASAPost>? = null
)