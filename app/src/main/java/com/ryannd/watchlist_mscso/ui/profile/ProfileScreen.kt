package com.ryannd.watchlist_mscso.ui.profile

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfileScreen() {
    Button(onClick = { Firebase.auth.signOut() }) {
        Text(text = "Logout")
    }
}