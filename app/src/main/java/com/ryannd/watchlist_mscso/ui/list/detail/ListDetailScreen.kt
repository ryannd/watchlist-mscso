package com.ryannd.watchlist_mscso.ui.list.detail

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ryannd.watchlist_mscso.ui.nav.NavBarState

@Composable
fun ListDetailScreen(
    id: String,
    onComposing: (NavBarState) -> Unit,
    listViewModel: ListDetailViewModel = viewModel(factory = ListDetailViewModelFactory(id, onComposing))
) {

}