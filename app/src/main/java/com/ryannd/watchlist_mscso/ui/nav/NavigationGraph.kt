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
import com.ryannd.watchlist_mscso.ui.profile.ProfileScreen
import com.ryannd.watchlist_mscso.ui.search.SearchScreen

@Composable
fun NavigationGraph(navController: NavHostController, onComposing: (NavBarState) -> Unit) {
    NavHost(navController = navController, startDestination = Screens.ListScreen.route) {
        composable(Screens.ListScreen.route) {
            ListScreen(navigateTo = { navigateTo(it, navController) })
        }
        composable(Screens.Search.route) {
            SearchScreen(navigateTo = { navigateTo(it, navController) })
        }
        composable(Screens.Profile.route) {
            ProfileScreen()
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
    }
}

fun navigateTo(route: String, navController: NavController) {
    navController.navigate(route)
}