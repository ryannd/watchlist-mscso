package com.ryannd.watchlist_mscso.db.model

import com.google.firebase.firestore.DocumentId

data class MediaEntry(
    val mediaUid: Media,
    val status: String,
    val currentEpisode: Int,
    val currentSeason: Int,
    val userUid: String,
    @DocumentId var firestoreID: String = ""
)