package com.example.tmdbmovie.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.tmdbmovie.presentation.components.MovieCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigationToMovieDetail:(id: String,isFavorite: Boolean) -> Unit,
){
    val allPopularMovies = homeViewModel.allPopularMovies.collectAsLazyPagingItems()
    val allUpComingMovies = homeViewModel.allUpComingMovies.collectAsLazyPagingItems()

    val context = LocalContext.current

    LaunchedEffect(key1 = allPopularMovies.loadState) {

        if(
            allPopularMovies.loadState.refresh is LoadState.Error ||
            allUpComingMovies.loadState.refresh is LoadState.Error
            ) {

            Toast.makeText(
                context,
                "Loading Error ",
                Toast.LENGTH_LONG
            ).show()

        }

    }

    if(
        allPopularMovies.loadState.refresh is LoadState.Loading
        || allUpComingMovies.loadState.refresh is LoadState.Loading
        )
    {
        Column(
          modifier = Modifier
              .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            CircularProgressIndicator(
                modifier = Modifier
            )
        }

    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Popular Movies",
                style = MaterialTheme.typography.headlineLarge
            )
            LazyRow() {
                items(
                    items = allPopularMovies,
                    key = {movie -> movie.id}
                ) {movie ->
                    movie?.let {
                        MovieCard(
                            movie = movie,
                            onClickedMovieDetail = { id,isFavorite -> onNavigationToMovieDetail(id,isFavorite)},
                            onUpdateFavorite = {id -> homeViewModel.updateFavourite(id,!movie.isFavorite)}
                        )
                    }
                }
            }

            Text(
                text = "UpComing Movies",
                style = MaterialTheme.typography.headlineLarge
            )
            LazyRow() {
                items(
                    items = allUpComingMovies,
                    key = {movie -> movie.id}
                ) {movie ->
                    movie?.let {

                        MovieCard(
                            movie = movie,
                            onClickedMovieDetail = { id,isFavorite -> onNavigationToMovieDetail(id,isFavorite)},
                            onUpdateFavorite = {id -> homeViewModel.updateFavouriteUpComingMovie(id,!movie.isFavorite)}
                        )
                    }
                }
            }

        }
    }


}