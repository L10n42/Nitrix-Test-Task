package com.kappdev.nitrixtesttask.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kappdev.nitrixtesttask.ui.video_player.VideoPlayerScreen
import com.kappdev.nitrixtesttask.ui.videos_screen.VideosScreen
import com.kappdev.nitrixtesttask.viewmodel.VideoViewModel

/**
 * Represents the navigation graph of the application.
 */
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<VideoViewModel>()
    val videos by viewModel.videos.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Videos
    ) {
        composable<Screen.Videos> {
            val isLoading by viewModel.isLoading.collectAsState()
            VideosScreen(
                videos = videos,
                isLoading = isLoading,
                onRefreshRequest = { viewModel.refreshVideos() },
                onVideoClick = { index ->
                    navController.navigate(Screen.VideoPlayer(index))
                }
            )
        }

        composable<Screen.VideoPlayer> { backStack ->
            val videoIndex = backStack.toRoute<Screen.VideoPlayer>().videoIndex
            VideoPlayerScreen(navController, videos, videoIndex)
        }
    }
}