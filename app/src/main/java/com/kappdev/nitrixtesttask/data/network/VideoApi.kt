package com.kappdev.nitrixtesttask.data.network

import com.kappdev.nitrixtesttask.data.model.Video
import retrofit2.http.GET

interface VideoApi {

    @GET("/L10n42/240a55d50e6db80554e750c20f0c039f/raw/41126a46908e95eb88d080549b3ae8f5d51e61e8/videos.json")
    suspend fun getVideos(): List<Video>

}