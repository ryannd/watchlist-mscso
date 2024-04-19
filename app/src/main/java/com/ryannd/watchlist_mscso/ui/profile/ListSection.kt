package com.ryannd.watchlist_mscso.ui.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ryannd.watchlist_mscso.db.model.CustomList
import com.ryannd.watchlist_mscso.db.model.Review
import com.ryannd.watchlist_mscso.ui.components.CustomListItem

@Composable
fun ListSection(
    lists: List<CustomList>,
    navigateTo: (String) -> Unit
) {
    Spacer(modifier = Modifier.height(20.dp))

    ElevatedCard (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        lists.forEach {
            CustomListItem(list = it, navigateTo = navigateTo)
        }
    }
}