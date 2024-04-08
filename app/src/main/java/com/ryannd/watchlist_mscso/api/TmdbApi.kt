package com.ryannd.watchlist_mscso.api

import com.ryannd.watchlist_mscso.BuildConfig
import com.ryannd.watchlist_mscso.api.data.MovieDetail
import com.ryannd.watchlist_mscso.api.data.SearchResult
import com.ryannd.watchlist_mscso.api.data.ShowDetail
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("/3/search/multi")
    suspend fun getSearch(@Query("query") searchTerm: String) : SearchResponse

    @GET("/3/movie/{movieId}")
    suspend fun getMovieDetail(@Path("movieId") movieId: String) : MovieDetail

    @GET("/3/tv/{showId}")
    suspend fun getShowDetail(@Path("showId") showId: String) : ShowDetail

    class SearchResponse(
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
                .addInterceptor(AuthInterceptor(BuildConfig.TMDB_KEY))
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