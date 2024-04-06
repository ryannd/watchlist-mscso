package com.ryannd.watchlist_mscso.ui.search

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel()
) {
    val searchUiState by searchViewModel.uiState.collectAsState()

    Button(onClick = {searchViewModel.doSearch("test")}) {
        Text(text = "test api")
    }
    
    Text(text = searchUiState.currentSearchTerm)
}