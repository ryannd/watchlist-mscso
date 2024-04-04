package com.ryannd.watchlist_mscso.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ryannd.watchlist_mscso.ui.list.ListScreen
import com.ryannd.watchlist_mscso.ui.profile.ProfileScreen
import com.ryannd.watchlist_mscso.ui.search.SearchScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.ListScreen.route) {
        composable(Screens.ListScreen.route) {
            ListScreen()
        }
        composable(Screens.Search.route) {
            SearchScreen()
        }
        composable(Screens.Profile.route) {
            ProfileScreen()
        }
    }
}