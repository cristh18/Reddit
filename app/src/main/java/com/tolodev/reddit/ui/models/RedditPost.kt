package com.tolodev.reddit.ui.models

import android.os.Parcelable
import com.tolodev.reddit.network.models.ChildrenData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RedditPost(
    val title: String,
    val author: String,
    val numComments: Int,
    val createdUtc: Long,
    val thumbnail: String,
    var visited: Boolean
) : Parcelable {

    constructor(data: ChildrenData) : this(
        data.title,
        data.author,
        data.numComments,
        data.createdUtc,
        data.thumbnail,
        data.visited
    )
}
