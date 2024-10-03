package com.kappdev.nitrixtesttask.di

import android.content.Context
import androidx.room.Room
import com.kappdev.nitrixtesttask.data.database.VideoDao
import com.kappdev.nitrixtesttask.data.database.VideoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideVideoDatabase(@ApplicationContext context: Context): VideoDatabase {
        return Room.databaseBuilder(context, VideoDatabase::class.java, VideoDatabase.NAME).build()
    }

    @Provides
    @Singleton
    fun provideVideoDao(database: VideoDatabase): VideoDao {
        return database.getVideoDao()
    }

}