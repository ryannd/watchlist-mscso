package com.ryannd.watchlist_mscso

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ryannd.watchlist_mscso.auth.FirebaseAuthenticator
import com.ryannd.watchlist_mscso.ui.nav.BottomNavigationBar
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import com.ryannd.watchlist_mscso.ui.nav.NavigationGraph
import com.ryannd.watchlist_mscso.ui.nav.TopNavBar
import com.ryannd.watchlist_mscso.ui.theme.WatchlistmscsoTheme

class MainActivity : ComponentActivity() {
    lateinit var authenticator : FirebaseAuthenticator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            WatchlistmscsoTheme {
                val navController: NavHostController = rememberNavController()
                var navBarState by remember {
                    mutableStateOf(NavBarState())
                }

                LaunchedEffect(navController.currentBackStackEntryFlow) {
                    navController.currentBackStackEntryFlow.collect {
                        navBarState = NavBarState(title = navBarState.title, showTopBar = it.destination.route?.contains("detail_screen") ?: false)
                    }
                }

                Scaffold(
                    topBar = {
                             if(navBarState.showTopBar) {
                                 TopNavBar(navController = navController, navBarState = navBarState)
                             }
                    },
                    bottomBar = {
                        BottomNavigationBar(
                            navController = navController,
                            modifier = Modifier
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        NavigationGraph(navController = navController, onComposing = { navBarState = it })
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        authenticator = FirebaseAuthenticator(activityResultRegistry)
        lifecycle.addObserver(authenticator)
    }
}