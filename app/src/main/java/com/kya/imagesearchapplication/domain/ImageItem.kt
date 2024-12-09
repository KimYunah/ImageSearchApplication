package com.kya.imagesearchapplication.domain

data class ImageItem(
    val title: String?,
    val thumbnail: String?,
    val link: String?,
    var isBookmark: Boolean
)
