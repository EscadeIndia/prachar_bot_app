package com.prachaarbot.di

import com.google.gson.GsonBuilder
import com.prachaarbot.data.interceptors.AuthInterceptor
import com.prachaarbot.data.remote.LoginApi
import com.prachaarbot.data.remote.SentMessageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    private val baseUrl = "https://prachaarbot.com/api/"


    private val okHttpBuilder: OkHttpClient.Builder =
        OkHttpClient.Builder().readTimeout(1, TimeUnit.MINUTES).writeTimeout(1, TimeUnit.MINUTES)

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    fun providesLoginApi(
        authInterceptor: AuthInterceptor
    ): LoginApi {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpBuilder.addInterceptor(authInterceptor).addInterceptor(loggingInterceptor).build())
            .build().create(LoginApi::class.java)
    }

    @Provides
    fun providesSentMessageApi(
        authInterceptor: AuthInterceptor
    ): SentMessageApi {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
            .client(okHttpBuilder.addInterceptor(authInterceptor).addInterceptor(loggingInterceptor).build())
            .build().create(SentMessageApi::class.java)
    }
}