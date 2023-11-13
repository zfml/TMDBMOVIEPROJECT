package com.example.tmdbmovie.di

import android.content.Context
import androidx.room.Room
import com.example.tmdbmovie.data.local.MovieDatabase
import com.example.tmdbmovie.data.local.MovieDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context = context,
            MovieDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

}