package com.ryannd.watchlist_mscso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.internal.GsonBuildConfig
import com.ryannd.watchlist_mscso.ui.nav.NavigationBar
import com.ryannd.watchlist_mscso.ui.nav.NavigationGraph
import com.ryannd.watchlist_mscso.ui.theme.WatchlistmscsoTheme

class MainActivity : ComponentActivity() {
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
}