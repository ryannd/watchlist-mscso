package com.ryannd.watchlist_mscso.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel(),
    navigateTo: (String) -> Unit
) {
    val searchUiState by searchViewModel.uiState.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    Column {
        Row {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    searchViewModel.searchDebounced(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(5.dp)),
                trailingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search Icon") }
            )
        }
        LazyColumn {
            items(searchUiState.searchResults) { searchResult ->
                SearchResult(searchResult, navigateTo)
            }
        }
    }
}