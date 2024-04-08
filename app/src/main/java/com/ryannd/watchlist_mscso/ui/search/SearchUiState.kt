package com.ryannd.watchlist_mscso.ui.search

import com.ryannd.watchlist_mscso.api.data.SearchResult

data class SearchUiState(
    var searchResults: List<SearchResult> = listOf()
)