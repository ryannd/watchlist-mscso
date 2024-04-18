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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    Column {
        Row(
            modifier = Modifier
                .height(100.dp)
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column {
                Text(
                    text = uiState.list.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "List by: ${uiState.list.userName}",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        LazyColumn {
            items(uiState.list.content) {
                MediaItem(media = it, navigateTo = navigateTo)
            }
        }
    }
}