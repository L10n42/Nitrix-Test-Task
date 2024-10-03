package com.kappdev.nitrixtesttask.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * A helper class for managing cache-related preferences.
 *
 * @param context The application context used to access shared preferences.
 */
class CachePreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    /**
     * Retrieves the last refresh time from shared preferences.
     *
     * @return The timestamp (in milliseconds) of the last refresh.
     */
    fun getLastRefreshTime(): Long {
        return sharedPreferences.getLong(LAST_REFRESH_TIME_KEY, 0L)
    }

    /**
     * Updates the last refresh time in shared preferences to the current system time.
     */
    fun updateLastRefreshTime() = setLastRefreshTime(System.currentTimeMillis())

    /**
     * Stores the given timestamp as the last refresh time in shared preferences.
     *
     * @param time The timestamp (in milliseconds) to be stored.
     */
    private fun setLastRefreshTime(time: Long) {
        sharedPreferences.edit().putLong(LAST_REFRESH_TIME_KEY, time).apply()
    }

    companion object {
        private const val NAME = "cache_preferences"
        private const val LAST_REFRESH_TIME_KEY = "last_refresh_time"
    }
}