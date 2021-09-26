package com.example.domain.repositories

import com.example.domain.models.NASAImageModel
import io.reactivex.Single

interface RemoteSearchRepository {

    fun getNasaImageModelList() : Single<List<NASAImageModel>>
}