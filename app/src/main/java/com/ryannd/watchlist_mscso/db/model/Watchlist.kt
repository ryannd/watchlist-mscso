package com.ryannd.watchlist_mscso.db.model

data class Watchlist(
    var planning: List<String> = listOf(),
    var watching: List<String> = listOf(),
    var completed: List<String> = listOf()
) {
    operator fun get(status: String): List<String>? {
        return when (status) {
            "Planning" -> {
                this.planning
            }
            "Watching" -> {
                this.watching
            }
            "Completed" -> {
                this.completed
            }
            else -> {
                null
            }
        }
    }

    operator fun set(status: String, value: List<String>) {
        when (status) {
            "Planning" -> {
                this.planning = value
            }
            "Watching" -> {
                this.watching = value
            }
            "Completed" -> {
                this.completed = value
            }
        }
    }
}