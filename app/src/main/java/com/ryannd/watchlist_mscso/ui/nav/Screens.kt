package com.ryannd.watchlist_mscso.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens (
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null,
    val onBar: Boolean = false
) {
    object WatchlistScreen : Screens (
        route = "watchlist_screen?id={id}",
        title = "Watchlist",
        icon = Icons.AutoMirrored.Outlined.List,
        onBar = true
    )

    object Search : Screens (
        route = "search_screen",
        title = "Search",
        icon = Icons.Outlined.Search,
        onBar = true
    )

    object ListDetail : Screens (
        route = "list/detail?id={id}",
        title = "List Detail"
    )

    object List : Screens (
        route = "list",
        title = "Lists",
        icon = Icons.Default.Create,
        onBar = true
    )

    object Profile : Screens (
        route = "user?id={id}",
        title = "Profile",
        icon = Icons.Outlined.Face,
        onBar = true
    )

    object Detail : Screens (
        route = "detail_screen/id={id}&type={type}",
        title = "Details"
    )
}