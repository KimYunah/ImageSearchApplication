package com.kya.imagesearchapplication.framework.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    @SerialName("lastBuildDate")
    val lastBuildDate: String?,
    @SerialName("total")
    val total: Int?,
    @SerialName("start")
    val start: Int?,
    @SerialName("display")
    val display: Int?,
    @SerialName("items")
    val items: List<Item>?
)