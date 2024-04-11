package com.ryannd.watchlist_mscso.db.model

data class Watchlist(
    val planning: List<String> = listOf(),
    val watching: List<String> = listOf(),
    val completed: List<String> = listOf()
)