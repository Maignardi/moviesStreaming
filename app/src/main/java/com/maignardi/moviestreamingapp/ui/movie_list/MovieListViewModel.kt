package com.maignardi.moviestreamingapp.ui.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maignardi.moviestreamingapp.domain.model.Movie
import com.maignardi.moviestreamingapp.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieListViewModel(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            getMoviesUseCase()
                .catch { exception ->
                    _error.value = exception.message
                    _movies.value = emptyList()
                }
                .collect { result ->
                    _movies.value = result
                }
            _isLoading.value = false
        }
    }
}
