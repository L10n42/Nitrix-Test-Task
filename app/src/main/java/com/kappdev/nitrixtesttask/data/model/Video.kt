package com.kappdev.nitrixtesttask.data.model

import com.kappdev.nitrixtesttask.data.database.VideoEntity

data class Video(
    val uid: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val duration: String,
    val uploadTime: String,
)

fun Video.toVideoEntity(): VideoEntity {
    return VideoEntity(
        uid = this.uid,
        title = this.title,
        description = this.description,
        thumbnailUrl = this.thumbnailUrl,
        videoUrl = this.videoUrl,
        duration = this.duration,
        uploadTime = this.uploadTime
    )
}