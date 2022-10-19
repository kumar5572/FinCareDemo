package com.example.fincaredemo.repository

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.concurrent.TimeUnit


interface Api {
    @GET("api/")
    fun getUserList(@QueryMap params: Map<String, String>): Call<ResponseBody>

    companion object {
        fun initRetrofit(): Api {
            val api: Api

            val httpClient = OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .readTimeout(60000, TimeUnit.SECONDS)

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://randomuser.me/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
            api = retrofit.create(Api::class.java)
            return api
        }
    }
}