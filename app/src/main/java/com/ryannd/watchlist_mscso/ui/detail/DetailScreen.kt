package com.ryannd.watchlist_mscso.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.ryannd.watchlist_mscso.R
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

    val showAddDialog = remember { mutableStateOf(false) }

    val showReviewDialog = remember {
        mutableStateOf(false)
    }

    val showListDialog = remember {
        mutableStateOf(false)
    }

    if(showAddDialog.value) {
        MediaDialog(
            mediaType = mediaType,
            stateObj = uiState,
            onDismissRequest = {
                showAddDialog.value = false
                viewModel.updateEntry()
            },
            onAdd = viewModel::addToList,
            onEdit = viewModel::editEntry,
            onDelete = viewModel::deleteEntry)
    }

    if(showReviewDialog.value) {
        ReviewDialog(
            stateObj = uiState,
            onAdd = viewModel::addReview,
            onDelete = viewModel::deleteReview
        ) {
            showReviewDialog.value = false
            viewModel.updateEntry()
        }
    }

    if(showListDialog.value) {
        ListDialog(uiState.userLists, uiState.tmdbId, {
            showListDialog.value = false
        }, viewModel::addToCustomList)
    }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        showListDialog.value = true
                    },
                ) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "Add to List")
                }
                Spacer(modifier = Modifier.size(10.dp))
                SmallFloatingActionButton(
                    onClick = {
                        showReviewDialog.value = true
                    },
                ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_feedback_24), contentDescription = "Add Review")
                }
                Spacer(modifier = Modifier.size(10.dp))
                ExtendedFloatingActionButton(
                    onClick = {
                        showAddDialog.value = true
                    },
                    text = {
                        if(uiState.userEntry != null) {
                            Text("Edit entry")
                        } else {
                            Text("Add to list")
                        }
                    },
                    icon = {
                        if(uiState.userEntry != null) {
                            Icon(imageVector = Icons.Filled.Edit, contentDescription = "edit")
                        } else {
                            Icon(Icons.Filled.Add, "Floating action button.")
                        }
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(it)) {
            BoxWithConstraints {
                val maxHeight = 600.dp
                val topHeight: Dp = maxHeight * 2 / 3

                Box(modifier = Modifier
                    .requiredHeight(topHeight)
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w780/${uiState.backgroundUrl}",
                        contentDescription = uiState.title,
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
                    Box(modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF121212), Color.Transparent),
                                startY = 100F,

                                )
                        )
                        .requiredHeight(topHeight))
                }

                Box(modifier = Modifier
                    .align(Alignment.BottomCenter)) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w400/${uiState.posterUrl}",
                        contentDescription = uiState.title,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }
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