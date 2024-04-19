package com.ryannd.watchlist_mscso.ui.list.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryannd.watchlist_mscso.ui.components.MediaItem
import com.ryannd.watchlist_mscso.ui.detail.ObserveLifecycle
import com.ryannd.watchlist_mscso.ui.nav.NavBarState

@Composable
fun ListDetailScreen(
    id: String,
    onComposing: (NavBarState) -> Unit,
    navigateTo: (String) -> Unit,
    listViewModel: ListDetailViewModel = viewModel(factory = ListDetailViewModelFactory(id, onComposing))
) {
    val uiState by listViewModel.uiState.collectAsState()
    listViewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)

    var editOn by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editOn = !editOn
                }
            ) {
                Icon(imageVector = if(editOn) Icons.Default.Check else Icons.Default.Edit, contentDescription = "Edit toggle")
            }
        }
    ) {
        if(uiState.list.content.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
            ) {
                items(uiState.list.content) {
                    MediaItem(media = it, navigateTo = navigateTo, editOn = editOn, onDelete = listViewModel::deleteFromList)
                }
            }
        } else {
            Text(text = "List is empty.", modifier = Modifier.fillMaxWidth().padding(it), textAlign = TextAlign.Center)
        }
    }
}