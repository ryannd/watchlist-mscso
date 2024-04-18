package com.ryannd.watchlist_mscso.ui.list.detail

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ryannd.watchlist_mscso.db.ListDbHelper
import com.ryannd.watchlist_mscso.db.model.CustomList
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
        listDb.getList(id) {
            val list = it.toObject(CustomList::class.java)
            if(list != null) {
                _uiState.update {state ->
                    state.copy(
                        list = list
                    )
                }
            }
        }
    }
}

class ListDetailViewModelFactory(private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ListDetailViewModel(id, onComposing) as T
}