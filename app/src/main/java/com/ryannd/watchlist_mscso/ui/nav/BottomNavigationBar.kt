package com.ryannd.watchlist_mscso.ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    val screens = listOf(
        Screens.ListScreen, Screens.Search, Screens.Profile
    )

   NavigationBar(
       modifier = modifier
   ) {
       val navBackStackEntry by navController.currentBackStackEntryAsState()
       val currentRoute = navBackStackEntry?.destination?.route

       screens.forEach {screen ->
           if(!screen.onBar) {
               return@forEach
           }
           NavigationBarItem(
               label = {
                   Text(text = screen.title!!)
               },
               icon = {
                   Icon(imageVector = screen.icon!!, contentDescription = "")
               },
               selected = currentRoute == screen.route,
               onClick = {
                   navController.navigate(screen.route) {
                       popUpTo(navController.graph.findStartDestination().id) {
                           saveState = true
                       }
                       launchSingleTop = true
                       restoreState = true
                   }
               },
               colors = NavigationBarItemDefaults.colors(
                   unselectedIconColor = Color.Gray, selectedTextColor = Color.White
               )
           )
       }
   }
}