package com.maignardi.moviestreamingapp.domain.model

data class Movie(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String
)