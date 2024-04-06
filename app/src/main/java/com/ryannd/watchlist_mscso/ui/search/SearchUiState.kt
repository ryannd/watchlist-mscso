package com.ryannd.watchlist_mscso.ui.search

import com.ryannd.watchlist_mscso.api.SearchResult

data class SearchUiState(
    var currentSearchTerm: String = "",
    var searchResults: List<SearchResult> = listOf()
)