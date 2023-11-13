package com.example.tmdbmovie.presentation.movieDetail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbmovie.data.mappers.toMovieEntity
import com.example.tmdbmovie.data.network.MovieDto
import com.example.tmdbmovie.data.repository.MovieRepository
import com.example.tmdbmovie.presentation.util.Constants.IS_FAVORITE
import com.example.tmdbmovie.presentation.util.Constants.MOVIE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val movieId: String = checkNotNull(savedStateHandle[MOVIE_ID])
    private val isFavorite: Boolean = checkNotNull(savedStateHandle[IS_FAVORITE])

     var uiState by  mutableStateOf(MovieDetailUiState())
     private set

    init {
        viewModelScope.launch {
            uiState = movieRepository.getMovieById(movieId).toMovieDetailUiState(isFavorite)


        }
    }


    fun updateFavourite(id: String,isFavorite: Boolean) {
        viewModelScope.launch {
            movieRepository.updateFavorite(id,isFavorite)
        }



        uiState = uiState.copy(
            isFavorite = !isFavorite
        )



    }

}
fun MovieDto.toMovieDetailUiState(isFavorite: Boolean) =
    MovieDetailUiState(
            id = id,
            title = title,
            releaseDate = release_date,
            posterPath = poster_path,
            isFavorite = isFavorite
    )

data class MovieDetailUiState(
    val id: String = "",
    val title: String = "",
    val releaseDate: String = "",
    val posterPath: String = "",
    val isFavorite: Boolean = false
)