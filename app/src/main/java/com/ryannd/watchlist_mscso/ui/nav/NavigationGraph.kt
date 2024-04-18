package com.ryannd.watchlist_mscso.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ryannd.watchlist_mscso.ui.detail.DetailScreen
import com.ryannd.watchlist_mscso.ui.list.ListScreen
import com.ryannd.watchlist_mscso.ui.list.detail.ListDetailScreen
import com.ryannd.watchlist_mscso.ui.profile.ProfileScreen
import com.ryannd.watchlist_mscso.ui.search.SearchScreen
import com.ryannd.watchlist_mscso.ui.watchlist.WatchlistScreen

@Composable
fun NavigationGraph(navController: NavHostController, onComposing: (NavBarState) -> Unit) {
    NavHost(navController = navController, startDestination = "watchlist_screen?id={id}") {
        composable(
            route = "watchlist_screen?id={id}",
            arguments = listOf(
                navArgument("id") {
                    nullable = true
                    defaultValue = ""
                    type = NavType.StringType
                }
            )
        ) {
            val arguments = requireNotNull(it.arguments)
            val id = arguments.getString("id") ?: ""

            WatchlistScreen(id = id, onComposing = onComposing, navigateTo = { navigateTo(it, navController) })
        }
        composable(Screens.Search.route) {
            SearchScreen(navigateTo = { navigateTo(it, navController) })
        }
        composable(
            route = "user?id={id}",
            arguments = listOf(
                navArgument("id") {
                    nullable = true
                    defaultValue = ""
                    type = NavType.StringType
                }
            )
        ) {
            val arguments = requireNotNull(it.arguments)
            val id = arguments.getString("id") ?: ""

            ProfileScreen(id, onComposing, { navigateTo(it, navController) })
        }
        composable(
            route = Screens.Detail.route,
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                },
                navArgument("type"){
                    type = NavType.StringType
                }
            )
        ) {
            val arguments = requireNotNull(it.arguments)
            val type = arguments.getString("type") ?: ""
            val id = arguments.getString("id") ?: ""

            DetailScreen(type, id, onComposing)
        }

        composable(
            route = Screens.ListDetail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) {
            val arguments = requireNotNull(it.arguments)
            val id = arguments.getString("id") ?: ""

            ListDetailScreen(id, onComposing, { navigateTo(it, navController) })
        }

        composable(
            route = Screens.List.route
        ) {
            ListScreen(navigateTo = { navigateTo(it, navController) })
        }
    }
}

fun navigateTo(route: String, navController: NavController) {
    navController.navigate(route)
}