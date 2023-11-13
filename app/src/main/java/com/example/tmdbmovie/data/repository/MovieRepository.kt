package com.example.tmdbmovie.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.tmdbmovie.data.local.MovieDatabase
import com.example.tmdbmovie.data.local.MovieRemoteKeyDao
import com.example.tmdbmovie.data.local.model.MovieEntity
import com.example.tmdbmovie.data.local.model.MovieRemoteKey
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingMovieEntity
import com.example.tmdbmovie.data.mappers.toMovieEntity
import com.example.tmdbmovie.data.network.MovieApi
import com.example.tmdbmovie.data.network.MovieDto
import com.example.tmdbmovie.data.network.MovieRemoteMediator
import com.example.tmdbmovie.data.network.UpComingMovieRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
){
   fun getAllPopularMovies(): Flow<PagingData<MovieEntity>> {
       val pagingSourceFactory = {movieDatabase.movieDao().getAllPopularMovies()}

       return Pager(
           config = PagingConfig(pageSize = 20),
           remoteMediator = MovieRemoteMediator(
               movieApi = movieApi,
               movieDatabase = movieDatabase
           ),
           pagingSourceFactory = pagingSourceFactory
       ).flow
   }

    fun getAllUpComingMovies(): Flow<PagingData<UpComingMovieEntity>> {
        val pagingSourceFactory = {movieDatabase.upComingMovieDao().getAllUpComingMovies()}

        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = UpComingMovieRemoteMediator(
                movieApi = movieApi,
                movieDatabase = movieDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getMovieById(id: String): MovieDto {
       return withContext(Dispatchers.IO) {
            movieApi.getMovieDetailById(id)
        }

    }

    suspend fun updateFavoriteOfUpComingMovie(id: String, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            movieDatabase.upComingMovieDao().updateMovieAsFavorite(id, isFavorite)
        }
    }

   suspend fun updateFavorite(id: String, isFavorite: Boolean) {
       withContext(Dispatchers.IO) {
           movieDatabase.movieDao().updateMovieAsFavorite(id, isFavorite)
       }
   }

}