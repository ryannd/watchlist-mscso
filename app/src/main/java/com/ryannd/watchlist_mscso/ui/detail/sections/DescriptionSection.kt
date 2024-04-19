package com.ryannd.watchlist_mscso.ui.detail.sections

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionSection(
    description: String
) {
    Text("Description", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.size(20.dp))
    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Text(
            text = description,
            maxLines = 7,
            modifier = Modifier.padding(20.dp),
            overflow = TextOverflow.Ellipsis
        )
    }
}