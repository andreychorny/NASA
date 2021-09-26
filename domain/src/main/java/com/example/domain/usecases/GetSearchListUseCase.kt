package com.example.domain.usecases

import com.example.domain.models.NASAImageModel
import com.example.domain.repositories.RemoteSearchRepository
import io.reactivex.Single
import javax.inject.Inject

class GetSearchListUseCase @Inject constructor(private val apiRepo: RemoteSearchRepository):
    SingleUseCase<List<NASAImageModel>> {

    override fun execute(): Single<List<NASAImageModel>> {
        return apiRepo.getNasaImageModelList()
    }

}