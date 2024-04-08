package com.ryannd.watchlist_mscso.api.data

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title", alternate = ["original_title"])
    val title: String,
    @SerializedName("backdrop_path")
    val backgroundUrl: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("poster_path")
    val posterUrl: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("runtime")
    val runtime: Int
)
