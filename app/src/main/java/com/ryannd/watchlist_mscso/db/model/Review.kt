package com.ryannd.watchlist_mscso.db.model

import com.google.firebase.firestore.DocumentId

data class Review(
    val userUid: String = "",
    val userName: String = "",
    val tmdbId: String = "",
    val liked: Boolean = false,
    val text: String = "",
    val title: String = "",
    @DocumentId var firestoreID: String = ""
)