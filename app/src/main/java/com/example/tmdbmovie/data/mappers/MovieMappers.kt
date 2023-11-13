package com.example.tmdbmovie.data.mappers

import com.example.tmdbmovie.data.Movie
import com.example.tmdbmovie.data.local.model.MovieEntity
import com.example.tmdbmovie.data.local.model.upComingMovie.UpComingMovieEntity
import com.example.tmdbmovie.data.network.MovieDto

fun MovieDto.toMovieEntity(isFavorite: Boolean = false) = MovieEntity(
    id = id,
    title = title,
    releaseDate = release_date,
    posterPath = poster_path,
    isFavorite = isFavorite

)

fun MovieEntity.toMovie() = Movie(
   id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = posterPath,
    isFavorite = isFavorite
)

fun MovieDto.toUpComingMovieEntity(isFavorite: Boolean = false) = UpComingMovieEntity(
    id = id,
    title = title,
    releaseDate = release_date,
    posterPath = poster_path,
    isFavorite = isFavorite

)

fun UpComingMovieEntity.toMovie() = Movie(
    id = id,
    title = title,
    releaseDate = releaseDate,
    posterPath = posterPath,
    isFavorite = isFavorite
)
