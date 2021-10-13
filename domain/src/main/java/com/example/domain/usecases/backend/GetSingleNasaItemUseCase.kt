package com.example.domain.usecases.backend

import com.example.domain.models.backend.NASAImageModel
import com.example.domain.repositories.RemoteSearchRepository
import com.example.domain.usecases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetSingleNasaItemUseCase @Inject constructor(private val apiRepo: RemoteSearchRepository):
    SingleUseCase<NASAImageModel, String> {

    //input - nasaId
    override fun execute(input: String): Single<NASAImageModel> {
        return apiRepo.getNasaModel(input)
    }

}