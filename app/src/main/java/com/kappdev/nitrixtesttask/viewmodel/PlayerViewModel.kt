package com.kappdev.nitrixtesttask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.kappdev.nitrixtesttask.data.model.Video
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted private val videos: List<Video>,
    @Assisted private val playVideoIndex: Int,
    val player: Player
) : ViewModel() {

    private val _selectedVideo = MutableStateFlow<Video?>(null)
    val selectedVideo: StateFlow<Video?> = _selectedVideo

    init {
        player.addListener(
            object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    _selectedVideo.value = videos[player.currentPeriodIndex]
                }
            }
        )
        addVideosToPlayer()
        player.seekTo(playVideoIndex, 0)
        player.prepare()
    }

    private fun addVideosToPlayer() {
        val mediaItems = videos.map { MediaItem.fromUri(it.videoUrl) }
        player.addMediaItems(mediaItems)
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }

    @AssistedFactory
    interface Factory {
        fun create(videos: List<Video>, playVideoIndex: Int): PlayerViewModel
    }
}