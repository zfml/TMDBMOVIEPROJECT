package com.example.tmdbmovie.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingMovieEntity

@Dao
interface UpComingMovieDao {
    @Upsert
    suspend fun upsertAll(movies: List<UpComingMovieEntity>)

    @Query("SELECT * FROM upcomingmovieentity")
    fun getAllUpComingMovies(): PagingSource<Int, UpComingMovieEntity>

    @Query("DELETE FROM upcomingmovieentity")
    suspend fun clearAll()

    @Query("SELECT * FROM upcomingmovieentity WHERE id=:id")
    suspend fun getMovieById(id: String): UpComingMovieEntity?

    @Query("Update upcomingmovieentity SET isFavorite =:isFavorite WHERE id=:id")
    suspend fun updateMovieAsFavorite(id: String, isFavorite: Boolean)
}