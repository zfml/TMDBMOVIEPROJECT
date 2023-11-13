package com.example.tmdbmovie.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.example.tmdbmovie.data.mappers.toMovie
import com.example.tmdbmovie.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {
    val allPopularMovies = movieRepository.getAllPopularMovies().map { it.map { it.toMovie() } }

    val allUpComingMovies = movieRepository.getAllUpComingMovies().map { it.map { it.toMovie() } }

    fun updateFavourite(id: String,isFavorite: Boolean) {
        viewModelScope.launch {
            movieRepository.updateFavorite(id,isFavorite)
        }
    }

    fun updateFavouriteUpComingMovie(id: String,isFavorite: Boolean) {
        viewModelScope.launch {
            movieRepository.updateFavoriteOfUpComingMovie(id,isFavorite)
        }
    }
}