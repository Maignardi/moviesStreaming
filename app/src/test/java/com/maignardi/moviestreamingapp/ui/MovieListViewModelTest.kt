package com.maignardi.moviestreamingapp.ui

import com.maignardi.moviestreamingapp.domain.model.Movie
import com.maignardi.moviestreamingapp.domain.usecase.GetMoviesUseCase
import com.maignardi.moviestreamingapp.ui.movie_list.MovieListViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    private lateinit var viewModel: MovieListViewModel
    private val getMoviesUseCase: GetMoviesUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadMovies - sucesso - atualiza lista e desativa loading`() = runTest {
        val movies = listOf(
            Movie("1", "Filme 1", "Desc 1", "videoUrl1", "thumb1"),
            Movie("2", "Filme 2", "Desc 2", "videoUrl2", "thumb2")
        )

        coEvery { getMoviesUseCase() } returns flow { emit(movies) }

        viewModel = MovieListViewModel(getMoviesUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.isLoading.value)
        assertEquals(null, viewModel.error.value)
        assertEquals(movies, viewModel.movies.value)
    }

    @Test
    fun `loadMovies - falha - atualiza erro e limpa lista`() = runTest {
        val exception = Exception("Erro de rede")
        coEvery { getMoviesUseCase() } returns flow { throw exception }

        viewModel = MovieListViewModel(getMoviesUseCase)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(false, viewModel.isLoading.value)
        assertEquals("Erro de rede", viewModel.error.value)
        assertEquals(emptyList<Movie>(), viewModel.movies.value)
    }
}
