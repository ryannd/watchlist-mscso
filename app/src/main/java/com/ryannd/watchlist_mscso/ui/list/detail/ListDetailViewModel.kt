package com.ryannd.watchlist_mscso.ui.list.detail

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ryannd.watchlist_mscso.ui.nav.NavBarState

class ListDetailViewModel(
    private val id: String,
    private val onComposing: (NavBarState) -> Unit
) : ViewModel(), DefaultLifecycleObserver {

}

class ListDetailViewModelFactory(private val id: String, private val onComposing: (NavBarState) -> Unit) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = ListDetailViewModel(id, onComposing) as T
}