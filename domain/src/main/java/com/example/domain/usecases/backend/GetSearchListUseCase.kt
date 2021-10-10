package com.example.domain.usecases.backend

import com.example.domain.models.backend.NASAImageModel
import com.example.domain.repositories.RemoteSearchRepository
import com.example.domain.usecases.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class GetSearchListUseCase @Inject constructor(private val apiRepo: RemoteSearchRepository):
    SingleUseCase<List<NASAImageModel>, String> {

    override fun execute(input: String): Single<List<NASAImageModel>> {
        return apiRepo.getNasaImageModelList(input)
    }

}