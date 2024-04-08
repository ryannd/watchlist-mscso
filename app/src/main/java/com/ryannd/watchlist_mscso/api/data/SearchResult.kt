package com.ryannd.watchlist_mscso.api.data

import java.io.Serializable
import com.google.gson.annotations.SerializedName
data class SearchResult(
    @SerializedName("id")
    val id: String,
    @SerializedName("backdrop_path")
    val backgroundUrl: String,
    @SerializedName("name", alternate=["title"])
    val title: String,
    @SerializedName("overview")
    val description: String,
    @SerializedName("poster_path")
    val posterUrl: String,
    @SerializedName("media_type")
    val type: String,
    @SerializedName(value="first_air_date", alternate=["release_date"])
    val releaseDate: String,
    @SerializedName("known_for")
    val knownFor: List<SearchResult>?
): Serializable
