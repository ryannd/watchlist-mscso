package com.ryannd.watchlist_mscso.api.data

import com.google.gson.annotations.SerializedName

data class Seasons(
    @SerializedName("episode_count")
    val episodeCount: Int,
    @SerializedName("season_number")
    val seasonNumber: Int,
    @SerializedName("name")
    val title: String
)
