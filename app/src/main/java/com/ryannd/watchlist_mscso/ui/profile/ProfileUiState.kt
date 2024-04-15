package com.ryannd.watchlist_mscso.ui.profile

import com.ryannd.watchlist_mscso.db.model.User

data class ProfileUiState(
    var user: User? = null,
    var isFollowing: Boolean = false
)
