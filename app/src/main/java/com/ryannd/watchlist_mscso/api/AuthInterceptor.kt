package com.ryannd.watchlist_mscso.api

import android.content.pm.ApplicationInfo
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer " + this.apiKey)
            .build()

        return chain.proceed(newRequest)
    }

}