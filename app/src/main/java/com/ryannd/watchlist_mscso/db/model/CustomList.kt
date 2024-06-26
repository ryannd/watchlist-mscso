package com.ryannd.watchlist_mscso.db.model

import com.google.firebase.firestore.DocumentId

data class CustomList(
    var name: String = "",
    var userName: String = "",
    var userId: String = "",
    var content: List<Media> = listOf(),
    val lookup: HashMap<String, Boolean> = HashMap(),
    @DocumentId var firestoreID: String = ""
)
