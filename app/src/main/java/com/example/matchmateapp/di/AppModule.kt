package com.example.matchmateapp.di

import android.content.Context
import androidx.room.Room
import com.example.matchmateapp.data.local.MatchMateDatabase
import com.example.matchmateapp.data.remote.api.MatchApiService
import com.example.matchmateapp.data.repository.MatchRepositoryImpl
import com.example.matchmateapp.domain.repository.MatchRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMatchApi(): MatchApiService =
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MatchApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MatchMateDatabase =
        Room.databaseBuilder(
            context,
            MatchMateDatabase::class.java,
            "matchmate.db"
        ).build()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMatchRepository(
        impl: MatchRepositoryImpl
    ): MatchRepository
}
