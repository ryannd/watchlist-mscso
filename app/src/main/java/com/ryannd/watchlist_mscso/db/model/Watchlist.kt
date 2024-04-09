package com.ryannd.watchlist_mscso.db.model

data class Watchlist(
    val planning: List<MediaEntry> = listOf(),
    val watching: List<MediaEntry> = listOf(),
    val completed: List<MediaEntry> = listOf()
)