package com.ryannd.watchlist_mscso.api

import android.content.pm.ApplicationInfo
import androidx.core.os.BuildCompat
import com.ryannd.watchlist_mscso.BuildConfig
import okhttp3.Authenticator
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.coroutines.coroutineContext

interface TmdbApi {
    @GET("/3/search/multi")
    suspend fun getSearch(@Query("query") searchTerm: String) : SearchResponse

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