package com.ryannd.watchlist_mscso.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ryannd.watchlist_mscso.db.MediaDbHelper
import com.ryannd.watchlist_mscso.db.model.Media

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMediaDialog(
    mediaType: String,
    stateObj: DetailUiState,
    onDismissRequest: () -> Unit
) {
    var statusExpanded by remember {
        mutableStateOf(false)
    }
    var status by remember {
        mutableStateOf("Planning")
    }

    val dbHelper = MediaDbHelper()

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties(usePlatformDefaultWidth = false),) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stateObj.title,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
            ExposedDropdownMenuBox(expanded = statusExpanded, onExpandedChange = { statusExpanded = !statusExpanded }) {

                TextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    modifier = Modifier.menuAnchor()
                )

              ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                  DropdownMenuItem(
                      text = { Text("Planning") },
                      onClick = {
                          status = "Planning"
                      }
                  )
                  HorizontalDivider()
                  DropdownMenuItem(
                      text = { Text("Watching") },
                      onClick = {
                          status = "Watching"
                      }
                  )
                  HorizontalDivider()
                  DropdownMenuItem(
                      text = { Text("Completed") },
                      onClick = {
                          status = "Completed"
                      }
                  )
              }
            }
            Row(
                Modifier
                    .padding(8.dp)
                    .fillMaxWidth(), Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                Button(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(text = "CANCEL")
                }
                Button(
                    onClick = {
                        val newMedia = Media(
                            type = mediaType,
                            background = stateObj.backgroundUrl,
                            seasons = if(mediaType == "tv") stateObj.seasons else null,
                            description = stateObj.description,
                            numSeasons = if(mediaType == "tv") stateObj.numSeasons else null,
                            title = stateObj.title,
                            tmdbId = stateObj.tmdbId,
                            poster = stateObj.posterUrl
                        )
                        dbHelper.addNewMediaToList(newMedia, status, onDismissRequest)
                    }
                ) {
                    Text(text = "ADD")
                }
            }
        }
    }

}