package com.example.tmdbmovie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingRemoteKey

@Dao
interface UpComingRemoteKeyDao {
    @Query("SELECT * FROM upcomingremotekey WHERE id=:id")
    suspend fun getMoviesRemoteKeysById(id: String): UpComingRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllMovieRemoteKeys(remoteKeys:List<UpComingRemoteKey>)

    @Query("DELETE FROM upcomingremotekey")
    suspend fun clearAllRemoteKeys()
}