package com.ryannd.watchlist_mscso.ui.detail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import com.ryannd.watchlist_mscso.db.model.CustomList
import com.ryannd.watchlist_mscso.db.model.Media

@Composable
fun ListDialog(
    lists: List<CustomList>,
    tmdbId: String,
    onDismissRequest: () -> Unit,
    addToLists: (listIds: List<String>, checked: Map<String, Boolean>, onComplete: () -> Unit) -> Unit
) {
    var checked by remember {
        mutableStateOf(lists.associate {
            it.firestoreID to it.lookup.contains(tmdbId)
        })
    }
    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
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
                Text(
                    text = "Add to list",
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                LazyColumn {
                    items(lists) {list ->
                        var isIncluded by remember {
                            mutableStateOf(false)
                        }

                        if(list.lookup.contains(tmdbId)) {
                            isIncluded = true
                        }

                        ListItem(
                            leadingContent = {
                                Checkbox(
                                    checked = isIncluded,
                                    onCheckedChange = {
                                        checked = if(it) {
                                            val new = checked.toMutableMap()
                                            new[list.firestoreID] = true
                                            new
                                        } else {
                                            val new = checked.toMutableMap()
                                            new[list.firestoreID] = false
                                            new
                                        }

                                        isIncluded = it
                                    }
                                )
                            },
                            headlineContent = {
                                Text(list.name)
                            },
                            supportingContent = {
                                Text(text = "${list.content.size} items")
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        onDismissRequest()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(text = "CANCEL")
                }
                Spacer(modifier = Modifier.size(20.dp))
                Button(
                    onClick = {
                        addToLists(checked.keys.toList(), checked) {
                            onDismissRequest()
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color("#5dbea3".toColorInt()))
                ) {
                    Text(text = "UPDATE")
                }
            }


        }
    }
}