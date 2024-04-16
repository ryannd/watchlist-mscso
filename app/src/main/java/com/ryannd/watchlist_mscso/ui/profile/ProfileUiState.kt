package com.ryannd.watchlist_mscso.ui.profile

import com.ryannd.watchlist_mscso.db.model.Media
import com.ryannd.watchlist_mscso.db.model.Review
import com.ryannd.watchlist_mscso.db.model.User

data class ProfileUiState(
    var user: User? = null,
    var isFollowing: Boolean = false,
    var reviews: List<Review> = listOf(),
    var mediaLookup: HashMap<String, Media> = HashMap()
)
