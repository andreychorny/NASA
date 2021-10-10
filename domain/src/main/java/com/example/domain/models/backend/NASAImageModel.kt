package com.example.domain.models.backend

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NASAImageModel(
    val nasaId: String,
    val title: String,
    val description: String,
    val dateCreated: String,
    val imageUrl: String,
) : Parcelable