package com.example.codingtest.retrofit

import com.example.codingtest.Payload
import retrofit2.Call
import retrofit2.http.*


interface IDataService {
    @GET("api/v1/aggTrades")
    fun getPayloads(@Query("symbol") symbol: String, @Query("limit") limit: Int = 500): Call<List<Payload>>
}