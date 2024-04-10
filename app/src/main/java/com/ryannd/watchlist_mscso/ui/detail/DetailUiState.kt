package com.ryannd.watchlist_mscso.ui.detail

import com.ryannd.watchlist_mscso.api.data.Seasons

data class DetailUiState(
    var mediaType: String = "",
    var title: String = "",
    var backgroundUrl: String = "",
    var description: String = "",
    var posterUrl: String = "",
    var releaseDate: String? = null,
    var runtime: Int? = null,
    var seasons: List<Seasons>? = null,
    var numSeasons: Int? = null,
    var tmdbId: String = ""
)