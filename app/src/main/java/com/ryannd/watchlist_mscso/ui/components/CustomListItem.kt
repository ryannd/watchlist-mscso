package com.ryannd.watchlist_mscso.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ryannd.watchlist_mscso.db.model.CustomList

@Composable
fun CustomListItem(
    list: CustomList,
    navigateTo: (String) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(list.name)
        },
        modifier = Modifier.clickable {
            navigateTo("list/detail?id=${list.firestoreID}")
        },
        supportingContent = {
            Text(text = "By: ${list.userName} | ${list.content.size} items")
        }
    )

    HorizontalDivider()
}