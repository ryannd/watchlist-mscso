package com.ryannd.watchlist_mscso.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens (
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object ListScreen : Screens (
        route = "list_screen",
        title = "Watchlist",
        icon = Icons.AutoMirrored.Outlined.List
    )

    object Search : Screens (
        route = "search_screen",
        title = "Search",
        icon = Icons.Outlined.Search
    )

    object Profile : Screens (
        route = "profile_screen",
        title = "Profile",
        icon = Icons.Outlined.Search
    )
}