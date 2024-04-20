package com.ryannd.watchlist_mscso.ui.detail

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import androidx.core.text.isDigitsOnly
import com.ryannd.watchlist_mscso.ui.components.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDialog(
    mediaType: String,
    stateObj: DetailUiState,
    onDismissRequest: () -> Unit,
    onAdd: (stateObj: DetailUiState, status: String, currEpisode: String, currSeason: String, rating: Int, onDismissRequest: () -> Unit) -> Unit,
    onEdit: (stateObj: DetailUiState, status: String, currEpisode: String, currSeason: String, rating: Int, onDismissRequest: () -> Unit) -> Unit,
    onDelete: (stateObj: DetailUiState, onDismissRequest: () -> Unit) -> Unit
) {
    var statusExpanded by remember {
        mutableStateOf(false)
    }
    var status by remember {
        mutableStateOf(stateObj.userEntry?.status ?: "Planning")
    }

    var episode by remember {
        mutableStateOf(if(stateObj.userEntry?.currentEpisode != null) stateObj.userEntry?.currentEpisode.toString() else "")
    }

    var season by remember {
        mutableStateOf(if(stateObj.userEntry?.currentSeason != null) stateObj.userEntry?.currentSeason.toString() else "")
    }

    var rating by remember {
        mutableIntStateOf(stateObj.userEntry?.rating ?: 0)
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
                    text = stateObj.title,
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.size(20.dp))

                Text(text = "Current status", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.size(5.dp))
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded },

                ) {

                    TextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(expanded = statusExpanded, onDismissRequest = { statusExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Planning") },
                            onClick = {
                                status = "Planning"
                                statusExpanded = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Watching") },
                            onClick = {
                                status = "Watching"
                                statusExpanded = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Completed") },
                            onClick = {
                                if(mediaType == "tv") {
                                    season = stateObj.numSeasons.toString()
                                    episode = stateObj.seasons?.get(
                                        season.toInt() - 1)?.episodeCount.toString()
                                }
                                status = "Completed"
                                statusExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                if(mediaType == "tv") {
                    Text(text = "Current season", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.size(5.dp))
                    TextField(
                        value = season,
                        onValueChange = { newText ->
                            if(newText.isEmpty() || (newText.isDigitsOnly() && newText.toInt() <= stateObj.numSeasons!!)) {
                                season = newText
                                episode = ""
                            }
                        },
                        enabled = status != "Planning",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Season number") },
                        suffix = { if(stateObj.numSeasons != null) Text(text= "/${stateObj.numSeasons}") }
                    )

                    Spacer(modifier = Modifier.size(20.dp))

                    Text(text = "Episodes watched", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.size(5.dp))
                    TextField(
                        value = episode,
                        onValueChange = { newText ->

                           if(newText.isEmpty() || (season != "" && newText.isDigitsOnly() && newText.toInt() <= (stateObj.seasons?.get(
                                   season.toInt() - 1
                               )?.episodeCount ?: -1))
                           ) {
                               episode = newText
                           }

                        },
                        enabled = status != "Planning",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Episode number") },
                        suffix = {
                            if(season != "" && stateObj.seasons?.get(season.toInt() - 1)?.episodeCount != null) {
                                Text(
                                    text = "/${(stateObj.seasons?.get(season.toInt() - 1)?.episodeCount)}"
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.size(20.dp))

                Column(Modifier.fillMaxWidth()) {
                    Text(text = "Rating", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.size(5.dp))
                    val starsColor = if(status != "Completed") Color.Gray else Color.Yellow
                    RatingBar(rating = rating, starsColor = starsColor, enabled = status != "Planning") {
                        rating = it
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

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


                if(stateObj.userEntry != null) {
                    Button(
                        onClick = {
                            onEdit(stateObj, status, episode, season, rating, onDismissRequest)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color("#5dbea3".toColorInt()))
                    ) {
                        Text(text = "EDIT")
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {
                            onDelete(stateObj, onDismissRequest)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color("#dd7973".toColorInt()))
                    ) {
                        Text(text = "DELETE")
                    }
                } else {
                    Button(
                        onClick = {
                            onAdd(stateObj, status, episode, season, rating, onDismissRequest)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color("#5dbea3".toColorInt()))
                    ) {
                        Text(text = "ADD")
                    }
                }
            }
        }
    }

}