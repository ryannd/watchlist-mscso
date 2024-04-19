package com.ryannd.watchlist_mscso.ui.detail.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ryannd.watchlist_mscso.api.data.Seasons

@Composable
fun SeasonsSection(
    seasons: List<Seasons>
) {
    Text("Seasons", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.size(20.dp))
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column {
            seasons.forEach {
                ListItem(
                    headlineContent = { Text(text = it.title) },
                    overlineContent = { Text(text = "Season ${it.seasonNumber}")},
                    trailingContent = { Text(text = "${it.episodeCount} episodes")}
                )
            }
        }
    }
}