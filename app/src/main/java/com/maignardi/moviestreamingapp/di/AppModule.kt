package com.maignardi.moviestreamingapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.maignardi.moviestreamingapp.data.remote.FirebaseMovieService
import com.maignardi.moviestreamingapp.data.repository.MovieRepositoryImpl
import com.maignardi.moviestreamingapp.domain.repository.MovieRepository
import com.maignardi.moviestreamingapp.domain.usecase.GetMovieByIdUseCase
import com.maignardi.moviestreamingapp.domain.usecase.GetMoviesUseCase
import com.maignardi.moviestreamingapp.ui.movie_detail.MovieDetailViewModel
import com.maignardi.moviestreamingapp.ui.movie_list.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import android.app.Application

val appModule = module {

    // Firebase
    single { FirebaseFirestore.getInstance() }
    single { com.google.firebase.storage.FirebaseStorage.getInstance() }

    // Remote service
    single { FirebaseMovieService(get(), get()) }

    // Repository
    single<MovieRepository> { MovieRepositoryImpl(get()) }

    // UseCases
    single { GetMoviesUseCase(get()) }
    single { GetMovieByIdUseCase(get()) }

    // ViewModels
    viewModel { MovieListViewModel(get()) }
    viewModel { MovieDetailViewModel(get<Application>(), get()) }
}
