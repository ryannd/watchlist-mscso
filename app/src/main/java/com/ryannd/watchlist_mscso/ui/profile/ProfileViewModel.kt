package com.ryannd.watchlist_mscso.ui.profile

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.db.UserDbHelper
import com.ryannd.watchlist_mscso.db.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(ProfileUiState())
    private val userDbHelper = UserDbHelper()
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchUser()
    }

    private fun fetchUser() {
        val uuid = Firebase.auth.currentUser?.uid
        if(uuid != null) {
            userDbHelper.getUserData(uuid) {
                val user = it.toObject(User::class.java)
                Log.d("ProfileViewModel", user.toString())
                if(user != null) {
                    _uiState.value = ProfileUiState(user = user)
                }
            }
        }
    }
}