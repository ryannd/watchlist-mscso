package com.ryannd.watchlist_mscso.ui.list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.db.ListDbHelper
import com.ryannd.watchlist_mscso.db.model.CustomList
import com.ryannd.watchlist_mscso.ui.profile.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListViewModel : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(ListUiState())
    val listDbHelper = ListDbHelper()
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()
    fun newList(name: String, onComplete: () -> Unit) {
        val currUser = Firebase.auth.currentUser
        if(currUser != null) {
            val list = CustomList(name = name, userName = currUser.displayName ?: "", userId = currUser.uid)
            listDbHelper.createList(list) {
                onComplete()
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getLists()
    }

    fun getLists() {
        listDbHelper.getAllLists { result ->
            _uiState.update {
                it.copy(
                    allSearchResults = result
                )
            }
        }

        listDbHelper.getUserLists { results ->
            _uiState.update {
                it.copy(
                    mySearchResults = results
                )
            }
        }
    }
}