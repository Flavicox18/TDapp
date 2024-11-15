package com.flavicox.tdapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    //private const val BASE_URL = "https://turismodias-api.onrender.com/"
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}