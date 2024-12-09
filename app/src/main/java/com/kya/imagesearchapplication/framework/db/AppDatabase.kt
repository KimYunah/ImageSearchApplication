package com.kya.imagesearchapplication.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kya.imagesearchapplication.framework.data.BookmarkEntity

@Database(entities = [BookmarkEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
}