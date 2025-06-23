package com.maignardi.moviestreamingapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ErrorText(message: String?, modifier: Modifier = Modifier) {
    if (!message.isNullOrEmpty()) {
        Text(
            text = "Erro: $message",
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
    }
}