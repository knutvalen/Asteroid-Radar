package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.domainmodels.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NearEarthObjectWebService {//TODO: rename

    @GET("neo/rest/v1/feed")
    fun getFeed(
        @Query("api_key") apiKey: String,
        @Query("start_date") startDate: String
    ): Call<String>

}

interface AstronomyPictureOfTheDayService {

    @GET("planetary/apod")
    fun getAstronomyPictureOfTheDay(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Deferred<PictureOfDay>

}

object APIService {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val nearEarthObject : NearEarthObjectWebService by lazy {
        retrofit.create(NearEarthObjectWebService::class.java)
    }

    val astronomyPictureOfTheDay : AstronomyPictureOfTheDayService by lazy {
        retrofit.create(AstronomyPictureOfTheDayService::class.java)
    }

}