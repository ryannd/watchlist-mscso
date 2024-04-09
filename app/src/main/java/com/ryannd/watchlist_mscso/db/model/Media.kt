package com.ryannd.watchlist_mscso.db.model

import com.google.firebase.firestore.DocumentId
import com.ryannd.watchlist_mscso.api.data.Seasons

data class Media(
    var title: String,
    var description: String,
    var type: String,
    var id: String,
    var poster: String,
    var seasons: Season?,
    var numSeasons: Int?,
    @DocumentId var firestoreID: String = ""
)