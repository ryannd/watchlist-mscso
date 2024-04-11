package com.ryannd.watchlist_mscso.api.data

import com.google.gson.annotations.SerializedName
import com.ryannd.watchlist_mscso.db.model.Season

data class ShowDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val title: String,
    @SerializedName("backdrop_path")
    val backgroundUrl: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("poster_path")
    val posterUrl: String,
    @SerializedName("seasons")
    val seasons: List<Season>,
    @SerializedName("number_of_seasons")
    val numSeasons: Int
)
