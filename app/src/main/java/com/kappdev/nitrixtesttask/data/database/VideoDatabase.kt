package com.kappdev.nitrixtesttask.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [VideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VideoDatabase : RoomDatabase() {

    abstract fun getVideoDao(): VideoDao

    companion object {
        const val NAME = "video_database"
    }
}