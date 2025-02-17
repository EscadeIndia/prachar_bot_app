package com.prachaarbot.data.interceptors

import com.prachaarbot.data.local.sharedPreference.LocalPrefData
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


internal class AuthInterceptor @Inject constructor(
    private val localPrefData: LocalPrefData
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = localPrefData.authToken.takeIf { it.isNotBlank() }
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.header("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}
