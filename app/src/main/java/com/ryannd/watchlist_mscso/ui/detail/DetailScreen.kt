package com.ryannd.watchlist_mscso.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ryannd.watchlist_mscso.ui.nav.NavBarState

@Composable
fun DetailScreen(
    mediaType: String,
    id: String,
    onComposing: (NavBarState) -> Unit,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory(mediaType, id, onComposing)),

) {
    viewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)
    val uiState by viewModel.uiState.collectAsState()
    val title = uiState.movieDetail?.title ?: uiState.showDetail?.title ?: ""
    val description = uiState.movieDetail?.description ?: uiState.showDetail?.description ?: ""
    val background = uiState.movieDetail?.backgroundUrl ?: uiState.showDetail?.backgroundUrl ?: ""
    val poster = uiState.movieDetail?.posterUrl ?: uiState.showDetail?.posterUrl ?: ""

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        BoxWithConstraints {
            val maxHeight = 600.dp
            val topHeight: Dp = maxHeight * 2 / 3

            Box(modifier = Modifier
                .requiredHeight(topHeight)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w780/$background",
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topHeight)
                        .blur(
                            radiusX = 5.dp,
                            radiusY = 5.dp,
                        ),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
                Box(modifier = Modifier.matchParentSize().background(Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color.Transparent),
                    startY = 100F,

                )).requiredHeight(topHeight))
            }

            Box(modifier = Modifier
                .align(Alignment.BottomCenter)) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w400/$poster",
                    contentDescription = title,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }

    }
}


@Composable
fun <LO : LifecycleObserver> LO.ObserveLifecycle(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@ObserveLifecycle)
        onDispose {
            lifecycle.removeObserver(this@ObserveLifecycle)
        }
    }
}