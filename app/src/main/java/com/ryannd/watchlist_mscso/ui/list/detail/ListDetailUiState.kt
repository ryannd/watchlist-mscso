package com.ryannd.watchlist_mscso.ui.list.detail

import com.ryannd.watchlist_mscso.db.model.Media

data class ListDetailUiState(
    var list: List<Media> = listOf()
)