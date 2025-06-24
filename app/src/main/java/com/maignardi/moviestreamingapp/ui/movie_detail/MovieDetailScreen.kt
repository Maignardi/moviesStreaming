package com.maignardi.moviestreamingapp.ui.movie_detail

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel

@OptIn(UnstableApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MovieDetailScreen(
    movieId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: MovieDetailViewModel = koinViewModel()
    val movie by viewModel.movie.collectAsState()
    var playVideo by rememberSaveable { mutableStateOf(false) }
    var videoPosition by rememberSaveable { mutableStateOf(0L) }

    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    BackHandler {
        if (playVideo) {
            showSystemUI(activity)
            playVideo = false
        } else {
            onBack()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        movie?.let { movie ->
            if (playVideo) {
                val exoPlayer = remember(movie.videoUrl) {
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(MediaItem.fromUri(movie.videoUrl.toUri()))
                        seekTo(videoPosition)
                        prepare()
                        playWhenReady = true
                    }
                }

                DisposableEffect(lifecycleOwner, exoPlayer) {
                    hideSystemUI(activity)
                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_PAUSE -> {
                                videoPosition = exoPlayer.currentPosition
                                exoPlayer.pause()
                            }
                            Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                            Lifecycle.Event.ON_DESTROY -> {
                                videoPosition = exoPlayer.currentPosition
                                exoPlayer.release()
                                showSystemUI(activity)
                            }
                            else -> {}
                        }
                    }

                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        videoPosition = exoPlayer.currentPosition
                        exoPlayer.release()
                        lifecycleOwner.lifecycle.removeObserver(observer)
                        showSystemUI(activity)
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = {
                            PlayerView(it).apply {
                                player = exoPlayer
                                useController = true
                                layoutParams = FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                keepScreenOn = true
                                setShutterBackgroundColor(android.graphics.Color.BLACK)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(Color.Black)
                        .padding(16.dp)
                ) {
                    TextButton(onClick = onBack) {
                        Text("\u2190 Voltar", style = MaterialTheme.typography.labelLarge, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = rememberAsyncImagePainter(movie.thumbnailUrl),
                        contentDescription = "Capa do filme",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = Color.White
                    )

                    Text(
                        text = movie.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 24.dp),
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Button(
                        onClick = { playVideo = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Assistir")
                    }
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@SuppressLint("WrongConstant")
@RequiresApi(Build.VERSION_CODES.R)
private fun hideSystemUI(activity: Activity) {
    WindowCompat.setDecorFitsSystemWindows(activity.window, false)
    WindowInsetsControllerCompat(activity.window, activity.window.decorView).apply {
        hide(WindowInsets.Type.systemBars())
        systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

@SuppressLint("WrongConstant")
@RequiresApi(Build.VERSION_CODES.R)
private fun showSystemUI(activity: Activity) {
    WindowCompat.setDecorFitsSystemWindows(activity.window, true)
    WindowInsetsControllerCompat(activity.window, activity.window.decorView).show(
        WindowInsets.Type.systemBars()
    )
    activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}