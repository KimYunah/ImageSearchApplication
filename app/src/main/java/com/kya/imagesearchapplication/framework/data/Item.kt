package com.kya.imagesearchapplication.framework.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("title")
    val title: String?,
    @SerialName("link")
    val link: String?,
    @SerialName("thumbnail")
    val thumbnail: String?,
    @SerialName("sizeheight")
    val sizeheight: String?,
    @SerialName("sizewidth")
    val sizewidth: String?
)