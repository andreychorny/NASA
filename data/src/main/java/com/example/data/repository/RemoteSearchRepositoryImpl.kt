package com.example.data.repository

import com.example.data.apiservice.ApiService
import com.example.data.mappers.asDomainModel
import com.example.domain.models.NASAImageModel
import com.example.domain.repositories.RemoteSearchRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteSearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): RemoteSearchRepository {

    override fun getNasaImageModelList(query: String): Single<List<NASAImageModel>> {
        return apiService.getSearchResponse(query).map { it.asDomainModel() }
    }
}