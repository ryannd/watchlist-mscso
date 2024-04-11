package com.ryannd.watchlist_mscso.db.model

import com.google.firebase.firestore.DocumentId
import com.ryannd.watchlist_mscso.api.data.Seasons

data class Media(
    var title: String = "",
    var description: String = "",
    var type: String = "",
    var tmdbId: String = "",
    var poster: String = "",
    var background: String = "",
    var seasons: List<Season>? = null,
    var numSeasons: Int? = null,
    @DocumentId var firestoreID: String = ""
)