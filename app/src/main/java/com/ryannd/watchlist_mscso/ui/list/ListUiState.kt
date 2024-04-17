package com.ryannd.watchlist_mscso.ui.list

import com.ryannd.watchlist_mscso.db.model.Media

data class ListUiState(
    var searchResults: List<Media> = listOf()
)