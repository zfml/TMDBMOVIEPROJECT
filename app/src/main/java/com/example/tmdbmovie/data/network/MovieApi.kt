package com.example.tmdbmovie.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("upcoming")
    suspend fun getAllUpComingMovies(
    @Query("page") page: Int
    ): MovieResult

    @GET("popular")
    suspend fun getAllPopularMovies(
        @Query("page") page: Int
    ): MovieResult

    @GET("{movie_id}")
    suspend fun getMovieDetailById(
        @Path("movie_id") id: String
    ): MovieDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/movie/"
    }


}