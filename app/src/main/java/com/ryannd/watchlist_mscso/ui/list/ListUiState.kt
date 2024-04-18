package com.ryannd.watchlist_mscso.ui.list

import com.ryannd.watchlist_mscso.db.model.CustomList

data class ListUiState(
    var allSearchResults: List<CustomList> = listOf(),
    var mySearchResults: List<CustomList> = listOf()
)