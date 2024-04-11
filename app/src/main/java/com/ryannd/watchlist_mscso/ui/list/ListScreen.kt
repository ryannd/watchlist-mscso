package com.ryannd.watchlist_mscso.ui.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryannd.watchlist_mscso.ui.detail.ObserveLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    listViewModel: ListViewModel = viewModel()
) {
    var selected by remember { mutableIntStateOf(0) }
    val titles = listOf("Planning", "Watching", "Completed")
    val listUiState = listViewModel.uiState.collectAsState()
    listViewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)

    Column {
        PrimaryTabRow(selectedTabIndex = selected) {
            titles.forEachIndexed { index, title ->
               Tab(selected = selected == index, onClick = { selected = index }, text = { Text(text = title) })
            }
        }

        LazyColumn {
            items(listUiState.value.completed ?: listOf()) {
                val media = listUiState.value.mediaLookup[it.mediaUid]
                if(media != null) {
                    Text(text = media.title)
                }
            }
        }
    }
}