package com.example.nasa.viewstate

import com.example.domain.models.backend.NASAImageModel

sealed class SearchResultViewState {

    object Loading: SearchResultViewState()

    data class Data(val items: List<NASAImageModel>): SearchResultViewState()

    data class Error(val message: String): SearchResultViewState()
}