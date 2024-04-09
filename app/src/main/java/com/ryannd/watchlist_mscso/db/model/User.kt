package com.ryannd.watchlist_mscso.db.model

import com.google.firebase.firestore.DocumentId

data class User(
    val userName: String = "",
    val followingList: List<String> = listOf(),
    val followerList: List<String> = listOf(),
    val watchlist: Watchlist = Watchlist(),
    val userUid: String,
    @DocumentId var firestoreID: String = ""
)