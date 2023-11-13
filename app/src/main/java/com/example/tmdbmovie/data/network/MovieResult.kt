package com.example.tmdbmovie.data.network

import com.google.gson.annotations.SerializedName

data class MovieResult(
    @SerializedName("results")
    val results : List<MovieDto>
)