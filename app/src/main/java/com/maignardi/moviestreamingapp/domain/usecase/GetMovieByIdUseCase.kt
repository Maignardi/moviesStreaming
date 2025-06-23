package com.maignardi.moviestreamingapp.domain.usecase

import com.maignardi.moviestreamingapp.domain.model.Movie
import com.maignardi.moviestreamingapp.domain.repository.MovieRepository

class GetMovieByIdUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(id: String): Movie? {
        return repository.getMovieById(id)
    }
}