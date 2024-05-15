package com.example.weatherapp


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//interface ApiService {
//    @GET("posts")
//    suspend fun getPosts(): List < Post >
//}

//interface WeatherApiService {
//
//    @GET("forecast")
//    suspend fun getWeather(
//        @Query("q") location: String,
//        @Query("appid") apiKey: String,
//        @Query("units") units: String = "metric"
//    ): WeatherResponse
//
//}

interface WeatherApiService {
    @GET("timeline/{location}/{date}")
    suspend fun getWeather(
        @Path("location") location: String,
        @Path("date") date: String,
        @Query("key") apiKey: String,
        @Query("elements") elements: String
    ): WeatherResponse2
}