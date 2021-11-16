package com.mvvm.networking

import android.content.Context
import com.mvvm.prefmanagers.Pref
import com.mvvm.utilities.PREF_KEY_AUTH
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder().addHeader(
            "oAuthkey", Pref.getValue(context = context, key = PREF_KEY_AUTH, defaultValue = "")
        ).build()

        return chain.proceed(requestBuilder)
    }
}