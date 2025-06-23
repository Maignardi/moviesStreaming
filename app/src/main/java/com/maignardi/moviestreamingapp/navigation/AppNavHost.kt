package com.maignardi.moviestreamingapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.maignardi.moviestreamingapp.ui.movie_detail.MovieDetailScreen
import com.maignardi.moviestreamingapp.ui.movie_list.MovieListScreen

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MovieList.route,
        modifier = modifier
    ) {
        composable(Screen.MovieList.route) {
            MovieListScreen(navController)
        }
        composable(
            route = Screen.MovieDetail.route + "/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) {
            val movieId = it.arguments?.getString("movieId") ?: return@composable
            MovieDetailScreen(movieId = movieId, onBack = { navController.popBackStack() })
        }
    }
}
