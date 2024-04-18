package com.ryannd.watchlist_mscso.ui.watchlist

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.db.EntryDbHelper
import com.ryannd.watchlist_mscso.db.UserDbHelper
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry
import com.ryannd.watchlist_mscso.db.model.User
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WatchlistViewModel(
    private val id: String,
    private val onComposing: (NavBarState) -> Unit
) : ViewModel(), DefaultLifecycleObserver {
    private val _uiState  = MutableStateFlow(WatchlistUiState())
    private val userDbHelper = UserDbHelper()
    private val entryDbHelper = EntryDbHelper()
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchLists()
    }

    private fun fetchLists() {
        val uuid: String? = if(id == "") {
            Firebase.auth.currentUser?.uid
        } else {
            id
        }

        if(uuid != null) {
            userDbHelper.getUserData(uuid) {
                val user = it.toObject(User::class.java)
                if(user != null) {
                    if(user.watchlist.completed.isNotEmpty()) {
                        entryDbHelper.getEntries(user.watchlist.completed) { query ->
                            _uiState.update {state ->
                                state.copy(completed = query.documents.mapNotNull {
                                    val entry = it.toObject(MediaEntry::class.java)
                                    entry
                                })
                            }
                        }
                    }

                   if(user.watchlist.planning.isNotEmpty()) {
                       entryDbHelper.getEntries(user.watchlist.planning) { query ->
                           _uiState.update {state ->
                               state.copy(planning = query.documents.mapNotNull {
                                   val entry = it.toObject(MediaEntry::class.java)
                                   entry
                               })
                           }
                       }
                   }

                   if(user.watchlist.watching.isNotEmpty()) {
                       entryDbHelper.getEntries(user.watchlist.watching) { query ->
                           _uiState.update {state ->
                               state.copy(watching = query.documents.mapNotNull {
                                   val entry = it.toObject(MediaEntry::class.java)
                                   entry
                               })
                           }
                       }
                   }
                    if(id != "") {
                        onComposing(NavBarState(title = "${user.userName}'s Watchlist", showTopBar = true))
                    }
                }
            }
        }
    }
}

class WatchlistViewModelFactory(private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = WatchlistViewModel(id, onComposing) as T
}