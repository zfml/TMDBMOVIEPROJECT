package com.example.tmdbmovie.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.tmdbmovie.data.local.model.MovieEntity
@Dao
interface MovieDao {
    @Upsert
    suspend fun upsertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movieentity")
    fun getAllPopularMovies(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movieentity")
    suspend fun clearAll()

    @Query("SELECT * FROM movieentity WHERE id=:id")
    suspend fun getMovieById(id: String): MovieEntity?

    @Query("Update movieentity SET isFavorite =:isFavorite WHERE id=:id")
    suspend fun updateMovieAsFavorite(id: String, isFavorite: Boolean)

}