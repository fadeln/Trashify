package com.example.trashify.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object ApiConfig {
    fun getApiService(token: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRJLTgyVENTb0tpbk1uUTYiLCJpYXQiOjE3MDMwOTA5NDR9.MMFJx06LQ_ndzETprqTx-kOMB7cXKojY5WhZmuY8xNM"): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = Interceptor{ chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://trashifycapstone.et.r.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

object RetrofitInstance {
    private const val BASE_URL = "http://ujicoba-406915.et.r.appspot.com/"

    val tutorialApiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

object ApiConfigPost {
    fun getApiServicePost(token: String? = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWRJLTgyVENTb0tpbk1uUTYiLCJpYXQiOjE3MDMwNzIxMTZ9.8lBfpa_dJVBXXcg5DBnoZ2vJPzdsHbu1HVC5FnGp0P8"): ApiServicePost {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = Interceptor{ chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiServicePost::class.java)
    }
}