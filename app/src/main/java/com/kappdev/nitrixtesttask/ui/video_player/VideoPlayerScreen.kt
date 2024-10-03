package com.kappdev.nitrixtesttask.ui.video_player

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.view.View
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.kappdev.nitrixtesttask.data.model.Video
import com.kappdev.nitrixtesttask.ui.util.SystemBarsController
import com.kappdev.nitrixtesttask.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    navController: NavHostController,
    videos: List<Video>,
    playVideoIndex: Int
) {
    val viewModel = hiltViewModel<PlayerViewModel, PlayerViewModel.Factory> { factory ->
        factory.create(videos, playVideoIndex)
    }

    val selectedVideo by viewModel.selectedVideo.collectAsState()

    var isControllerVisible by rememberSaveable { mutableStateOf(true) }
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val orientation = LocalConfiguration.current.orientation

    SystemBarsController(
        isSystemBarsVisible = orientation == ORIENTATION_PORTRAIT && isControllerVisible
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = viewModel.player
                    setShowFastForwardButton(false)
                    setShowRewindButton(false)
                    setControllerVisibilityListener(
                        PlayerView.ControllerVisibilityListener { visibility: Int ->
                            isControllerVisible = (visibility == View.VISIBLE)
                        }
                    )
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_STOP -> {
                        it.onPause()
                        it.player?.pause()
                    }
                    Lifecycle.Event.ON_START -> {
                        it.onResume()
                    }
                    else -> Unit
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        )

        selectedVideo?.let { currentVideo ->
            AnimatedPlayerTopBar(
                title = currentVideo.title,
                isVisible = isControllerVisible,
                modifier = Modifier.align(Alignment.TopCenter),
                onBack = { navController.navigateUp() }
            )
        }
    }
}

@Composable
private fun AnimatedPlayerTopBar(
    title: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 4.dp, start = 4.dp, end = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = "Back button",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = title,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}