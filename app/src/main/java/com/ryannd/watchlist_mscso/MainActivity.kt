package com.ryannd.watchlist_mscso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ryannd.watchlist_mscso.auth.FirebaseAuthenticator
import com.ryannd.watchlist_mscso.ui.nav.NavigationBar
import com.ryannd.watchlist_mscso.ui.nav.NavigationGraph
import com.ryannd.watchlist_mscso.ui.theme.WatchlistmscsoTheme

class MainActivity : ComponentActivity() {
    lateinit var authenticator : FirebaseAuthenticator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            WatchlistmscsoTheme {
                val navController: NavHostController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            navController = navController,
                            modifier = Modifier
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        NavigationGraph(navController = navController)
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