package com.ryannd.watchlist_mscso.ui.list.detail

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.ryannd.watchlist_mscso.db.ListDbHelper
import com.ryannd.watchlist_mscso.db.model.CustomList
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.ui.list.ListUiState
import com.ryannd.watchlist_mscso.ui.nav.NavBarState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ListDetailViewModel(
    private val id: String,
    private val onComposing: (NavBarState) -> Unit
) : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(ListDetailUiState())
    private val listDb = ListDbHelper()
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchList()
    }

    fun fetchList() {
        listDb.getList(id) {
            val list = it.toObject(CustomList::class.java)
            if(list != null) {
                onComposing(NavBarState(title="${list.name} - ${list.userName}", showTopBar = true))
                _uiState.update {state ->
                    state.copy(
                        list = list,
                        isOwner = list.userId == Firebase.auth.currentUser?.uid
                    )
                }
            }
        }
    }

    fun deleteFromList(media: Media, onComplete: () -> Unit) {
        val lookup = _uiState.value.list.lookup
        lookup.remove(media.tmdbId)
        val mutablelist = _uiState.value.list.content.toMutableList()
        mutablelist.removeAll {
            it.tmdbId == media.tmdbId
        }
        val updatedList = _uiState.value.list.copy(
            content = mutablelist,
            lookup = lookup
        )

        listDb.updateList(_uiState.value.list.firestoreID, updatedList) {
            fetchList()
            onComplete()
        }
    }

    fun deleteList(listId: String, onComplete: () -> Unit) {
        listDb.deleteList(listId) {
            onComplete()
        }
    }
}

class ListDetailViewModelFactory(private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ListDetailViewModel(id, onComposing) as T
}