package com.example.data.mappers
import com.example.data.models.response.ItemResponse
import com.example.domain.models.NASAImageModel

import com.example.data.models.response.SearchResponse

fun SearchResponse.asDomainModel(): List<NASAImageModel>{
    return collection.items.map { it.asDomainModel() }
}

fun ItemResponse.asDomainModel(): NASAImageModel{
    val firstDataEntry = data[0]
    return NASAImageModel(firstDataEntry.title, firstDataEntry.description,
        firstDataEntry.dateCreated, links[0].href)
}