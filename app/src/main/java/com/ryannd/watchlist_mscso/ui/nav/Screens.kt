package com.ryannd.watchlist_mscso.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens (
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null,
    val onBar: Boolean = false
) {
    object ListScreen : Screens (
        route = "list_screen",
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

    object Profile : Screens (
        route = "profile_screen",
        title = "Profile",
        icon = Icons.Outlined.Search,
        onBar = true
    )

    object Detail : Screens (
        route = "detail_screen/id={id}&type={type}",
        title = "Details"
    )
}