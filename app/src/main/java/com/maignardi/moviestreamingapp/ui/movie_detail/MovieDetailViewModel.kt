package com.maignardi.moviestreamingapp.ui.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maignardi.moviestreamingapp.domain.model.Movie
import com.maignardi.moviestreamingapp.domain.usecase.GetMovieByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MovieDetailViewModel(
    private val getMovieByIdUseCase: GetMovieByIdUseCase
) : ViewModel() {

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie

    fun loadMovie(id: String) {
        viewModelScope.launch {
            val result = getMovieByIdUseCase(id)
            _movie.value = result
        }
    }
}