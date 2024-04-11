package com.ryannd.watchlist_mscso.ui.list

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.MediaEntry

data class ListUiState(
    val planning: List<MediaEntry>? = null,
    val watching: List<MediaEntry>? = null,
    val completed: List<MediaEntry>? = null,
    val mediaLookup: SnapshotStateMap<String, Media> = SnapshotStateMap()
)