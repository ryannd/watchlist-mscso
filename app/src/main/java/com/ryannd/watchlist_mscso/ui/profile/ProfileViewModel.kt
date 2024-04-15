package com.ryannd.watchlist_mscso.ui.profile

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.db.UserDbHelper
import com.ryannd.watchlist_mscso.db.model.User
import com.ryannd.watchlist_mscso.ui.detail.DetailViewModel
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(
    private val id: String,
    private val onComposing: (NavBarState) -> Unit
) : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(ProfileUiState())
    private val userDbHelper = UserDbHelper()
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchUser()
    }

    fun logout() {
        Firebase.auth.signOut()
        fetchUser()
    }

    private fun fetchUser() {
        val uuid: String? = if(id == "") {
            Firebase.auth.currentUser?.uid
        } else {
            id
        }

        if(uuid != null) {
            userDbHelper.getUserData(uuid) {
                val user = it.toObject(User::class.java)
                Log.d("ProfileViewModel", user.toString())
                if(user != null) {
                    _uiState.value = ProfileUiState(user = user)
                    if(id != "") {
                        onComposing(NavBarState(title = "${user.userName}'s Profile", showTopBar = true))
                    }
                }
            }
        } else {
            _uiState.value = ProfileUiState(user = null)
        }
    }

}

class ProfileViewModelFactory(private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfileViewModel(id, onComposing) as T
}