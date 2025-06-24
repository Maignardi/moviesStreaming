package com.maignardi.moviestreamingapp.ui

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.maignardi.moviestreamingapp.domain.model.Movie
import com.maignardi.moviestreamingapp.domain.usecase.GetMovieByIdUseCase
import com.maignardi.moviestreamingapp.ui.movie_detail.MovieDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MovieDetailViewModelTest {

    private val getMovieByIdUseCase: GetMovieByIdUseCase = mockk()
    private val application: Application = ApplicationProvider.getApplicationContext()
    private lateinit var viewModel: MovieDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieDetailViewModel(application, getMovieByIdUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovie atualiza o StateFlow com o filme retornado`() = runTest {
        val movieId = "abc123"
        val expectedMovie = Movie(
            id = movieId,
            title = "Filme Teste",
            description = "Descrição",
            videoUrl = "videoUrl",
            thumbnailUrl = "thumbnailUrl"
        )

        coEvery { getMovieByIdUseCase(movieId) } returns expectedMovie

        viewModel.loadMovie(movieId)
        advanceUntilIdle()

        val result = viewModel.movie.first()
        assertEquals(expectedMovie, result)
    }
}
