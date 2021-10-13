package com.example.domain.repositories

import com.example.domain.models.backend.NASAImageModel
import io.reactivex.Single

interface RemoteSearchRepository {

    fun getNasaModel(nasaId: String): Single<NASAImageModel>

    fun getNasaImageModelList(query: String) : Single<List<NASAImageModel>>
}