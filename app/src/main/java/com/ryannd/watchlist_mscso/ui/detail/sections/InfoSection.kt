package com.ryannd.watchlist_mscso.ui.detail.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun InfoSection(
    mediaType: String,
    releaseDate: String?,
    runtime: Int?,
    numSeasons: Int?
) {
    Text("Information", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.size(20.dp))
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = mediaType.uppercase(Locale.ROOT), fontSize = 20.sp, modifier = Modifier.weight(1f))
            if(numSeasons != null) {
                VerticalDivider()
                Text(text = "$numSeasons SEASONS", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
            if(runtime != null) {
                VerticalDivider()
                Text(text = "$runtime MINS", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
            if(releaseDate != null) {
                VerticalDivider()
                Text(text = releaseDate.uppercase(Locale.ROOT), modifier = Modifier.weight(1f), textAlign = TextAlign.End)
            }

        }
    }

}