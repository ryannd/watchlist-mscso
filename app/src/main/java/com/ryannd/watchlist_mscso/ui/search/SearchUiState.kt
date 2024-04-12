package com.ryannd.watchlist_mscso.ui.search

import com.ryannd.watchlist_mscso.api.data.SearchResult
import com.ryannd.watchlist_mscso.db.model.User

data class SearchUiState(
    var mediaSearchResults: List<SearchResult> = listOf(),
    var peopleSearchResults: List<User> = listOf()
)