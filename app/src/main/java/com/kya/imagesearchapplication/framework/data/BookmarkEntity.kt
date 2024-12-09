package com.kya.imagesearchapplication.framework.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val title: String,
    val thumbnail: String,
    val link: String
)