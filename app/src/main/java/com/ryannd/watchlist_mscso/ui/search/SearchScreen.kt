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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = viewModel(),
    navigateTo: (String) -> Unit
) {
    val searchUiState by searchViewModel.uiState.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var selected by rememberSaveable { mutableIntStateOf(0) }
    Column {
        TextField(
            value = text,
            onValueChange = {
                text = it
                when (selected) {
                    0 -> searchViewModel.mediaSearchDebounced(it)
                    1 -> searchViewModel.peopleSearchDebounced(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .border(BorderStroke(1.dp, Color.Black), RoundedCornerShape(5.dp)),
            trailingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search Icon") }
        )
        PrimaryTabRow(selectedTabIndex = selected) {
            Tab(selected = selected == 0, onClick = { selected = 0 }, text = { Text(text = "Media") })
            Tab(selected = selected == 1, onClick = { selected = 1 }, text = { Text(text = "People") })
        }
        LazyColumn {
            when (selected) {
                0 -> items(searchUiState.mediaSearchResults) { searchResult ->
                    MediaSearchResult(searchResult, navigateTo)
                }
                1 -> items(searchUiState.peopleSearchResults) { searchResult ->
                    PeopleSearchResult(result = searchResult, navigateTo)
                }
            }
        }
    }
}