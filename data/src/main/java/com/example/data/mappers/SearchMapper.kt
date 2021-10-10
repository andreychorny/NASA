package com.example.data.mappers
import com.example.data.models.response.ItemResponse
import com.example.domain.models.backend.NASAImageModel

import com.example.data.models.response.SearchResponse

fun SearchResponse.asDomainModel(): List<NASAImageModel>{
    return collection.items.map { it.asDomainModel() }
}

fun ItemResponse.asDomainModel(): NASAImageModel {
    val firstDataEntry = data[0]
    return NASAImageModel(nasaId = firstDataEntry.nasa_id, title = firstDataEntry.title,
        description = firstDataEntry.description,
        dateCreated = firstDataEntry.dateCreated,
        imageUrl = links[0].href)
}