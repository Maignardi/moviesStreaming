package com.maignardi.moviestreamingapp.domain.usecase

import com.maignardi.moviestreamingapp.domain.repository.MovieRepository

class GetMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke() = repository.getMovies()
}