package com.ryannd.watchlist_mscso.ui.list.detail

import com.ryannd.watchlist_mscso.db.model.CustomList

data class ListDetailUiState(
    var list: CustomList = CustomList()
)