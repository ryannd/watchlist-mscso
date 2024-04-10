package com.ryannd.watchlist_mscso.db.model

import com.google.firebase.firestore.DocumentId

data class MediaEntry(
    val mediaUid: String,
    val mediaType: String,
    val status: String,
    val currentEpisode: Int? = null,
    val currentSeason: Int? = null,
    @DocumentId var firestoreID: String = ""
)