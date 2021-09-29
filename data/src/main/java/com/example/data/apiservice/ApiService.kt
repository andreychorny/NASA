package com.example.data.apiservice

import com.example.data.models.response.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search?media_type=image")
    fun getSearchResponse(@Query("q")query: String): Single<SearchResponse>

}