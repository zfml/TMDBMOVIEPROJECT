package com.example.tmdbmovie.data.network


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.tmdbmovie.data.local.MovieDatabase
import com.example.tmdbmovie.data.local.model.MovieEntity
import com.example.tmdbmovie.data.local.model.MovieRemoteKey
import com.example.tmdbmovie.data.mappers.toMovieEntity
import java.lang.Exception

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
): RemoteMediator<Int, MovieEntity>(){

    private val movieDao = movieDatabase.movieDao()
    private val movieKeyDao = movieDatabase.movieRemoteKeyDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
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

            val response = movieApi.getAllPopularMovies(page = currentPage)

            val movies = response.results

            val endOfPaginationReached = movies.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                    movieKeyDao.clearAllRemoteKeys()
                }
                val keys = movies.map { movie ->
                    MovieRemoteKey(
                        id = movie.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                movieKeyDao.addAllMovieRemoteKeys(remoteKeys = keys)

                val updatedMovies = movies.map { remoteMovie ->
                    val localMovie = movieDao.getMovieById(remoteMovie.id)

                    if (localMovie != null) {
                        remoteMovie.toMovieEntity(isFavorite = localMovie.isFavorite)
                    } else {
                        remoteMovie.toMovieEntity(isFavorite = false)
                    }
                }

                movieDao.upsertAll(movies = updatedMovies)


            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        }catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                movieKeyDao.getMoviesRemoteKeysById(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieEntity>
    ): MovieRemoteKey? {
        return state.pages.firstOrNull{ it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie ->
                movieKeyDao.getMoviesRemoteKeysById(id = movie.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int , MovieEntity>
    ): MovieRemoteKey? {

        return state.pages.lastOrNull{ it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                movieKeyDao.getMoviesRemoteKeysById(id = movie.id)
            }

    }

}