package com.example.domain.models.firebase

data class UserActivities(
    val likedPosts: HashMap<String, NASAPost>? = null,
    val subscriptions: HashMap<String, String>? = null,
    val subscribers: HashMap<String, String>? = null,
)