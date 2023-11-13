package com.example.tmdbmovie.data.local.model.upComingMovie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UpComingRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)