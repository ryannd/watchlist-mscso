package com.ryannd.watchlist_mscso.ui.detail

import com.ryannd.watchlist_mscso.api.data.Seasons
import com.ryannd.watchlist_mscso.db.model.CustomList
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry
import com.ryannd.watchlist_mscso.db.model.Review
import com.ryannd.watchlist_mscso.db.model.Season

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
    var tmdbId: String = "",
    var userEntry: MediaEntry? = null,
    var userReview: Review? = null,
    var userName: String = "",
    var userLists: List<CustomList> = listOf(),
    var reviews: List<Review> = listOf()
)