package com.maignardi.moviestreamingapp.ui.movie_detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.maignardi.moviestreamingapp.domain.model.Movie
import com.maignardi.moviestreamingapp.domain.usecase.GetMovieByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDetailViewModel(
    application: Application,
    private val getMovieByIdUseCase: GetMovieByIdUseCase
) : AndroidViewModel(application) {

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie

    val exoPlayer: ExoPlayer = ExoPlayer.Builder(application.applicationContext).build()
    private var isPlayerInitialized = false

    fun loadMovie(id: String) {
        viewModelScope.launch {
            val result = getMovieByIdUseCase(id)
            _movie.value = result

            if (!isPlayerInitialized && result != null) {
                withContext(Dispatchers.Main) {
                    exoPlayer.setMediaItem(MediaItem.fromUri(result.videoUrl))
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                    isPlayerInitialized = true
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
