package com.example.tmdbmovie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tmdbmovie.data.local.model.MovieEntity
import com.example.tmdbmovie.data.local.model.MovieRemoteKey
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingMovieEntity
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingRemoteKey

@Database(
    entities = [MovieEntity::class,MovieRemoteKey::class,UpComingMovieEntity::class,UpComingRemoteKey::class],
    version = 1
)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun movieRemoteKeyDao(): MovieRemoteKeyDao


    abstract fun upComingMovieDao(): UpComingMovieDao

    abstract fun upComingRemoteKeyDao(): UpComingRemoteKeyDao

    companion object{
        const val DATABASE_NAME = "movie_db"
    }
}