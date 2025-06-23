package com.maignardi.moviestreamingapp.data.model

import com.maignardi.moviestreamingapp.domain.model.Movie

data class MovieDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val thumbnailUrl: String = "",
    val videoPath: String = "", // do Firestore
    val videoUrl: String = ""   // será preenchido no app
) {
    fun toDomain(): Movie = Movie(
        id = id,
        title = title,
        description = description,
        thumbnailUrl = thumbnailUrl,
        videoUrl = videoUrl
    )
}