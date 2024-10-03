package com.kappdev.nitrixtesttask.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappdev.nitrixtesttask.data.model.Video
import com.kappdev.nitrixtesttask.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: VideoRepository
) : ViewModel() {

    private val _videos = MutableStateFlow<List<Video>>(emptyList())
    val videos: StateFlow<List<Video>> = _videos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var fetchVideosJob: Job? = null

    init {
        fetchVideos()
    }

    /**
     * Forces a refresh of the videos from the remote server.
     */
    fun refreshVideos() {
        fetchVideos(true)
    }

    private fun fetchVideos(forceRefresh: Boolean = false) {
        fetchVideosJob?.cancel()
        fetchVideosJob = viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            repository.getVideos(forceRefresh).collectLatest { newVideos ->
                _videos.value = newVideos
                _isLoading.value = false
            }
        }
    }
}