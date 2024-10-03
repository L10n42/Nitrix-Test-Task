package com.kappdev.nitrixtesttask.data.repository

import com.kappdev.nitrixtesttask.data.database.VideoDao
import com.kappdev.nitrixtesttask.data.database.VideoEntity
import com.kappdev.nitrixtesttask.data.database.toVideo
import com.kappdev.nitrixtesttask.data.model.Video
import com.kappdev.nitrixtesttask.data.model.toVideoEntity
import com.kappdev.nitrixtesttask.data.network.VideoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val videoApi: VideoApi,
    private val videoDao: VideoDao,
    private val cachePrefs: CachePreferencesHelper
) {

    /**
     * Fetches a list of videos, either from the local cache (if possible) or the remote server.
     *
     * @param forceRefresh Whether to force a refresh from the remote server.
     *
     * @return A [Flow] that emits the list of videos.
     */
    suspend fun getVideos(forceRefresh: Boolean = false): Flow<List<Video>> = flow {
        val cachedVideos = getCachedVideos()

        if (!forceRefresh) {
            emit(cachedVideos)
        }

        if (forceRefresh || cachedVideos.isEmpty() || shouldRefresh()) {
            try {
                val remoteVideos = getRemoteVideos()
                updateCache(remoteVideos)
                emit(remoteVideos)
            } catch (e: Exception) {
                if (forceRefresh) {
                    emit(cachedVideos)
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun updateCache(data: List<Video>) {
        val videoEntities = data.map(Video::toVideoEntity)
        videoDao.clearVideos()
        videoDao.insertVideos(videoEntities)
        cachePrefs.updateLastRefreshTime()
    }

    private suspend fun getRemoteVideos(): List<Video> {
        return videoApi.getVideos()
    }

    private suspend fun getCachedVideos(): List<Video> {
        return videoDao.getAllVideos().map(VideoEntity::toVideo)
    }

    /**
     * Checks whether the data should be refreshed based on the last cache refresh time.
     *
     * @return True if the cache is older than the expiration time.
     */
    private fun shouldRefresh(): Boolean {
        val lastRefreshTime = cachePrefs.getLastRefreshTime()
        val currentTime = System.currentTimeMillis()
        return currentTime - lastRefreshTime > CACHE_EXPIRATION_TIME
    }

    companion object {
        private const val CACHE_EXPIRATION_TIME = 5 * 60 * 1000
    }
}