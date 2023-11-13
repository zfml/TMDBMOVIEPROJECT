package com.example.tmdbmovie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbmovie.data.local.model.MovieRemoteKey
@Dao
interface MovieRemoteKeyDao {
    @Query("SELECT * FROM movieremotekey WHERE id=:id")
    suspend fun getMoviesRemoteKeysById(id: String): MovieRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMovieRemoteKeys(remoteKeys:List<MovieRemoteKey>)

    @Query("DELETE FROM movieremotekey")
    suspend fun clearAllRemoteKeys()
}