package com.ryannd.watchlist_mscso.db.model

data class Season(
    val mediaUid: String,
    val episodeCount: Int,
    val seasonNumber: Int,
    val title: String
)