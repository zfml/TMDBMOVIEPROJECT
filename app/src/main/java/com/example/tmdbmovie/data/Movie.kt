package com.example.tmdbmovie.data

data class Movie(
    val id: String,
    val title: String,
    val releaseDate: String,
    val posterPath: String,
    val isFavorite: Boolean = false
)
