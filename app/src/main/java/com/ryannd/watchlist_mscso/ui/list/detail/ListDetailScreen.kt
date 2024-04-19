package com.ryannd.watchlist_mscso.ui.list.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryannd.watchlist_mscso.ui.list.detail.MediaItem
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

    var showWarningDialog by remember {
        mutableStateOf(false)
    }

    if(showWarningDialog) {
        Dialog(
            onDismissRequest = {
                showWarningDialog = false
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text("Are you sure you want do delete this list?")
                    Row (
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                showWarningDialog = false
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "NO")
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                        Button(
                            onClick = {
                                listViewModel.deleteList(uiState.list.firestoreID) {
                                    navigateTo("list")
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color("#dd7973".toColorInt())),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "YES")
                        }
                    }

                    }
                }
        }
    }

    Scaffold(
        floatingActionButton = {
            if(uiState.isOwner) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                ) {
                    SmallFloatingActionButton(
                        onClick = {
                            showWarningDialog = true
                        },
                    ) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Add to List")
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    FloatingActionButton(
                        onClick = {
                            editOn = !editOn
                        }
                    ) {
                        Icon(
                            imageVector = if (editOn) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = "Edit toggle"
                        )
                    }
                }
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
            Text(text = "List is empty.", modifier = Modifier
                .fillMaxWidth()
                .padding(it), textAlign = TextAlign.Center)
        }
    }
}