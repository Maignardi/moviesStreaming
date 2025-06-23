package com.maignardi.moviestreamingapp.data.repository

import com.maignardi.moviestreamingapp.data.remote.FirebaseMovieService
import com.maignardi.moviestreamingapp.domain.model.Movie
import com.maignardi.moviestreamingapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(
    private val service: FirebaseMovieService
) : MovieRepository {

    override fun getMovies(): Flow<List<Movie>> = flow {
        val list = service.fetchMovies().map { it.toDomain() }
        emit(list)
    }

    override suspend fun getMovieById(id: String): Movie? {
        return service.fetchMovieById(id)?.toDomain()
    }
}