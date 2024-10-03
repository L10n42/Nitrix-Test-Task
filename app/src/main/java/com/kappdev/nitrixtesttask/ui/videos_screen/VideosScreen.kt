package com.kappdev.nitrixtesttask.ui.videos_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kappdev.nitrixtesttask.R
import com.kappdev.nitrixtesttask.data.model.Video

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen(
    videos: List<Video>,
    isLoading: Boolean,
    onRefreshRequest: () -> Unit,
    onVideoClick: (videoIndex: Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.your_videos))
                }
            )
        }
    ) { innerPadding ->
        VideosList(
            videos = videos,
            isLoading = isLoading,
            onVideoClick = onVideoClick,
            onRefreshRequest = onRefreshRequest,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideosList(
    videos: List<Video>,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onVideoClick: (videoIndex: Int) -> Unit,
    onRefreshRequest: () -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        LazyColumn(
            modifier = Modifier.matchParentSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(videos) { index, video ->
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(index) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(Unit) {
                onRefreshRequest()
            }
        }

        LaunchedEffect(isLoading) {
            when {
                isLoading -> pullToRefreshState.startRefresh()
                else -> pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}