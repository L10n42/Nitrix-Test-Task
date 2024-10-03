package com.kappdev.nitrixtesttask.navigation

import kotlinx.serialization.Serializable

/** A sealed class representing screens in the application. */
@Serializable
sealed class Screen {

    /**
     * Represents the video listing screen.
     */
    @Serializable
    data object Videos: Screen()

    /**
     * Represents the video player screen.
     *
     * @param videoIndex The index of the video in the list to start playing.
     */
    @Serializable
    data class VideoPlayer(val videoIndex: Int): Screen()

}