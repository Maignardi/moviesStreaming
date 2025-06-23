package com.maignardi.moviestreamingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.maignardi.moviestreamingapp.navigation.AppNavHost
import com.maignardi.moviestreamingapp.ui.theme.MoviestreamingappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviestreamingappTheme {
                AppNavHost()
            }
        }
    }
}