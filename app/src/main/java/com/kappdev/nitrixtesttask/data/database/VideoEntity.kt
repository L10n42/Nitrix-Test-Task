package com.kappdev.nitrixtesttask.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kappdev.nitrixtesttask.data.model.Video

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey
    @ColumnInfo("uid")
    val uid: String,

    @ColumnInfo("title")
    val title: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("thumbnail_url")
    val thumbnailUrl: String,

    @ColumnInfo("video_url")
    val videoUrl: String,

    @ColumnInfo("duration")
    val duration: String,

    @ColumnInfo("upload_time")
    val uploadTime: String
)

fun VideoEntity.toVideo(): Video {
    return Video(
        uid = this.uid,
        title = this.title,
        description = this.description,
        thumbnailUrl = this.thumbnailUrl,
        videoUrl = this.videoUrl,
        duration = this.duration,
        uploadTime = this.uploadTime
    )
}
