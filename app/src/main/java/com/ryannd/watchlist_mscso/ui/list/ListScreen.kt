package com.ryannd.watchlist_mscso.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryannd.watchlist_mscso.ui.components.CustomListItem
import com.ryannd.watchlist_mscso.ui.detail.ObserveLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    listViewModel: ListViewModel = viewModel(),
    navigateTo: (String) -> Unit
) {
    val uiState by listViewModel.uiState.collectAsState()
    listViewModel.ObserveLifecycle(LocalLifecycleOwner.current.lifecycle)

    var showDialog by remember {
        mutableStateOf(false)
    }

    if(showDialog) {
        AddListDialog(onAdd = listViewModel::newList) {
            showDialog = false
            listViewModel.getLists()
        }
    }

    var selected by rememberSaveable { mutableIntStateOf(0) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.padding(8.dp).fillMaxWidth()
        ) {

            IconButton(
                onClick = {
                          showDialog = true
                },
                modifier = Modifier.width(50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create list",
                    modifier = Modifier.size(30.dp))
            }
        }

        PrimaryTabRow(selectedTabIndex = selected) {
            Tab(selected = selected == 0, onClick = { selected = 0 }, text = { Text(text = "All") })
            Tab(selected = selected == 1, onClick = { selected = 1 }, text = { Text(text = "My Lists") })
        }

        LazyColumn {
            when (selected) {
                0 -> {
                    items(uiState.allSearchResults) {list ->
                       CustomListItem(list = list, navigateTo = navigateTo)
                    }
                }
                1 -> {
                    items(uiState.mySearchResults) {list ->
                        CustomListItem(list = list, navigateTo = navigateTo)
                    }
                }
            }
        }
    }
}