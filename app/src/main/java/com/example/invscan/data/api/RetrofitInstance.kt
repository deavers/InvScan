package com.example.invscan.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://www.deave.kz/api/"

    private val client = OkHttpClient.Builder()

    private val retrofit by lazy {

        val logging = HttpLoggingInterceptor()

        logging.level = (HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(logging).addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type","application/json" )
                .addHeader("Accept","application/json")
                .build()

            chain.proceed(request)
        }

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client.build())
            .build()
    }


    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }


}