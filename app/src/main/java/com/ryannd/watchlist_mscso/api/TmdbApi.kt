package com.ryannd.watchlist_mscso.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
interface TmdbApi {
    @GET("/3/search/multi?query={searchTerm}")
    suspend fun getSearch(@Query("searchTerm") searchTerm: String) : SearchResponse
    class SearchResponse(val data: SearchData)
    class SearchData(
        val page: Int,
        val results: List<SearchResult>,
        val totalPages: Int,
        val totalResults: Int
    )

    companion object {
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .build()

        fun create(): TmdbApi = create(url)
        private fun create(httpUrl: HttpUrl): TmdbApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TmdbApi::class.java)
        }
    }
}