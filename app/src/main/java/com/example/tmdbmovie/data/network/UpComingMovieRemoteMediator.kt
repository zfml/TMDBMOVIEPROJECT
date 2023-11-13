package com.example.tmdbmovie.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.tmdbmovie.data.local.MovieDatabase
import com.example.tmdbmovie.data.local.model.MovieEntity
import com.example.tmdbmovie.data.local.model.MovieRemoteKey
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingMovieEntity
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingRemoteKey
import com.example.tmdbmovie.data.mappers.toMovieEntity
import com.example.tmdbmovie.data.mappers.toUpComingMovieEntity
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class UpComingMovieRemoteMediator(
private val movieApi: MovieApi,
private val movieDatabase: MovieDatabase
): RemoteMediator<Int, UpComingMovieEntity>(){
    private val upComingMovieDao = movieDatabase.upComingMovieDao()
    private val upComingMovieKeyDao = movieDatabase.upComingRemoteKeyDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UpComingMovieEntity>,
    ): MediatorResult {
        return try{
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = movieApi.getAllUpComingMovies(page = currentPage)

            val movies = response.results

            val endOfPaginationReached = movies.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    upComingMovieDao.clearAll()
                    upComingMovieKeyDao.clearAllRemoteKeys()
                }
                val keys = movies.map { movie ->
                    UpComingRemoteKey(
                        id = movie.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                upComingMovieKeyDao.addAllMovieRemoteKeys(remoteKeys = keys)

                upComingMovieDao.upsertAll(movies = movies.map {
                    val localMovie = upComingMovieDao.getMovieById(it.id)

                    it.toUpComingMovieEntity(localMovie?.isFavorite?: false)
                })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        }catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UpComingMovieEntity>
    ): UpComingRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                upComingMovieKeyDao.getMoviesRemoteKeysById(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UpComingMovieEntity>
    ): UpComingRemoteKey? {
        return state.pages.firstOrNull{ it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
               upComingMovieKeyDao.getMoviesRemoteKeysById(id = movie.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UpComingMovieEntity>
    ): UpComingRemoteKey? {

        return state.pages.lastOrNull{ it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                upComingMovieKeyDao.getMoviesRemoteKeysById(id = movie.id)
            }

    }
}