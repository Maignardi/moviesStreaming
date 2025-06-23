package com.maignardi.moviestreamingapp.domain.repository


import com.maignardi.moviestreamingapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<List<Movie>>
    suspend fun getMovieById(id: String): Movie?
}