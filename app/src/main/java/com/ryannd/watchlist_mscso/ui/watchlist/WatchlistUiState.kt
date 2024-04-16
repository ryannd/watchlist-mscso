package com.ryannd.watchlist_mscso.ui.watchlist

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry

data class WatchlistUiState(
    val planning: List<MediaEntry>? = null,
    val watching: List<MediaEntry>? = null,
    val completed: List<MediaEntry>? = null,
    val mediaLookup: SnapshotStateMap<String, Media> = SnapshotStateMap()
)