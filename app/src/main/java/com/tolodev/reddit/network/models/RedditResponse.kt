package com.tolodev.reddit.network.models

import com.squareup.moshi.Json

data class RedditResponse(@Json(name = "data") val data: Data)

data class Data(
    @Json(name = "after") val after: String?,
    @Json(name = "dist") val dist: Int,
    @Json(name = "children") val children: List<Children>,
    @Json(name = "before") val before: String?
)

data class Children(@Json(name = "data") val data: ChildrenData)

data class ChildrenData(
    @Json(name = "author_fullname") val authorFullName: String,
    @Json(name = "title") val title: String,
    @Json(name = "created") val created: Long,
    @Json(name = "author") val author: String,
    @Json(name = "num_comments") val numComments: Int,
    @Json(name = "created_utc") val createdUtc: Long,
    @Json(name = "thumbnail") val thumbnail: String,
    @Json(name = "visited") val visited: Boolean
)
