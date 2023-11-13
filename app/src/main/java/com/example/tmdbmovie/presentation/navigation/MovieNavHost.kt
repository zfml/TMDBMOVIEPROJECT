package com.example.tmdbmovie.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdbmovie.presentation.home.HomeScreen
import com.example.tmdbmovie.presentation.movieDetail.MovieDetailScreen
import com.example.tmdbmovie.presentation.util.Constants.IS_FAVORITE
import com.example.tmdbmovie.presentation.util.Constants.MOVIE_DETAIL_SCREEN
import com.example.tmdbmovie.presentation.util.Constants.MOVIE_DETAIL_SCREEN_ARG
import com.example.tmdbmovie.presentation.util.Constants.MOVIE_HOME_SCREEN
import com.example.tmdbmovie.presentation.util.Constants.MOVIE_ID

@Composable
fun MovieNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = MOVIE_HOME_SCREEN
    ) {
        composable(
            route = MOVIE_HOME_SCREEN
        ) {
            HomeScreen(
                onNavigationToMovieDetail = { id,isFavorite ->
                    navController.navigate("${MOVIE_DETAIL_SCREEN}/$id/$isFavorite")
                }
            )
        }

        composable(
            route = MOVIE_DETAIL_SCREEN_ARG,
            arguments = listOf(
                navArgument(name = MOVIE_ID){
                    type = NavType.StringType
                },
                navArgument(name = IS_FAVORITE) {
                    type = NavType.BoolType
                }
            )
        ) {
            MovieDetailScreen()
        }

    }
}