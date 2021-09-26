package com.example.data.apiservice

import com.example.data.models.response.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ApiService {

    @GET("/search?q=apollo%11&media_type=image")
    fun getSearchResponse(): Single<SearchResponse>

}