package com.maignardi.moviestreamingapp.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.maignardi.moviestreamingapp.data.model.MovieDto
import kotlinx.coroutines.tasks.await


class FirebaseMovieService(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    suspend fun fetchMovies(): List<MovieDto> {
        val snapshot = firestore.collection("movies").get().await()
        val dtos = snapshot.documents.mapNotNull { it.toObject(MovieDto::class.java) }

        return dtos.map { dto ->
            val videoUrl = getVideoUrl(dto.videoPath)
            dto.copy(videoUrl = videoUrl)
        }
    }

    suspend fun fetchMovieById(id: String): MovieDto? {
        val doc = firestore.collection("movies").document(id).get().await()
        val dto = doc.toObject(MovieDto::class.java)
        return dto?.copy(videoUrl = getVideoUrl(dto.videoPath))
    }

    private suspend fun getVideoUrl(path: String): String {
        return storage.reference.child(path).downloadUrl.await().toString()
    }
}