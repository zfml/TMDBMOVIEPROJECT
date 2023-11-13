package com.example.tmdbmovie.data.local.model.upComingMovie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UpComingMovieEntity(
     @PrimaryKey
     val id: String,
     val title: String,
     val releaseDate: String,
     val posterPath: String,
     val isFavorite: Boolean = false
 )
