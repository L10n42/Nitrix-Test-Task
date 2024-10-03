package com.kappdev.nitrixtesttask.di

import com.kappdev.nitrixtesttask.data.network.NetworkConstants
import com.kappdev.nitrixtesttask.data.network.VideoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideVideoApi(): VideoApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NetworkConstants.BASE_URL)
            .build()
            .create(VideoApi::class.java)
    }

}