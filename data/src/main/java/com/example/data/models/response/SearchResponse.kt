package com.example.data.models.response

import com.squareup.moshi.Json

data class SearchResponse (
    val collection: CollectionResponse
)

data class CollectionResponse(
    val items: List<ItemResponse>,
    @Json(name = "links") val nextPageLink: List<CollectionLinkResponse>
)

data class ItemResponse(
    val data: List<DataResponse>,
    val links: List<ItemLinkResponse>
)

data class DataResponse(
    val title: String,
    @Json(name = "date_created") val dateCreated: String,
    val description: String,
    @Json(name = "media_type") val mediaType: String
)

data class ItemLinkResponse(
    val render: String,
    val href: String
)
data class CollectionLinkResponse(
    val rel: String,
    val href: String
)