package com.ryannd.watchlist_mscso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.auth.FirebaseAuthenticator
import com.ryannd.watchlist_mscso.db.model.User
import com.ryannd.watchlist_mscso.ui.nav.BottomNavigationBar
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import com.ryannd.watchlist_mscso.ui.nav.NavigationGraph
import com.ryannd.watchlist_mscso.ui.nav.TopNavBar
import com.ryannd.watchlist_mscso.ui.theme.WatchlistmscsoTheme

class MainActivity : ComponentActivity() {
    private lateinit var authenticator : FirebaseAuthenticator
    private val isAlertShowing = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val alertShowing = remember {
                isAlertShowing
            }

            WatchlistmscsoTheme {
                val navController = rememberNavController()
                var navBarState by remember {
                    mutableStateOf(NavBarState())
                }

                LaunchedEffect(navController.currentBackStackEntryFlow) {
                    navController.currentBackStackEntryFlow.collect {
                        navBarState = NavBarState(title = navBarState.title, showTopBar = it.destination.route?.contains("detail_screen") ?: false)
                    }
                }
                
                if(alertShowing.value) {
                    var userName by remember { mutableStateOf("") }
                    Dialog(
                        onDismissRequest = {
                            isAlertShowing.value = false
                        }
                    ) {
                        Surface(shape = MaterialTheme.shapes.medium) {
                            Column(Modifier.padding(24.dp)) {
                                Text(text = "Please enter a user name")
                                Spacer(Modifier.size(16.dp))
                                TextField(value = userName, onValueChange = { userName = it })
                                Row(
                                    Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(), Arrangement.spacedBy(8.dp, Alignment.End)
                                ) {
                                    Button(
                                        onClick = {
                                            if (authenticator.user() != null) {
                                                val newUser = User(
                                                    userName = userName,
                                                    userUid = authenticator.user()!!.uid
                                                )
                                                authenticator.userDbHelper.addNewUser(newUser)
                                                val profileUpdate = UserProfileChangeRequest.Builder()
                                                    .setDisplayName(userName)
                                                    .build()
                                                authenticator.user()!!.updateProfile(profileUpdate).addOnCompleteListener {
                                                    isAlertShowing.value = false
                                                    navController.navigate("watchlist_screen")
                                                }
                                            }
                                        }
                                    ) {
                                        Text(text = "SET")
                                    }
                                }
                            }
                        }
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
        authenticator = FirebaseAuthenticator(activityResultRegistry, isAlertShowing)
        lifecycle.addObserver(authenticator)
    }
}