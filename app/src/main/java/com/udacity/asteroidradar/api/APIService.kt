package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedService {

    @GET("neo/rest/v1/feed")
    fun getFeed(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String
    ): Call<String>

}

object APIService {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    val feed : FeedService by lazy { retrofit.create(FeedService::class.java) }

}