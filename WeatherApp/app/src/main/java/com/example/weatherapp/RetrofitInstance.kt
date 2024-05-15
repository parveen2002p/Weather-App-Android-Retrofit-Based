package com.example.weatherapp

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


object RetrofitClient {
    private const val BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/"

    val instance: WeatherApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WeatherApiService::class.java)
    }
}

//suspend fun fetchWeatherForDate(location: String, date: String, apiKey: String): WeatherResponse2 {
//    val pp= RetrofitClient.instance.getWeather(location, date, apiKey, "tempmax,tempmin,temp")
//    Log.d("MainActivity", "Item: $pp")
//    return pp
//}

suspend fun fetchWeatherForDate(location: String, date: String, apiKey: String): Pair<Double?, Double?>? {
    try {
        val response =
            RetrofitClient.instance.getWeather(location, date, apiKey, "tempmax,tempmin,temp")
        val weatherDay = response.days.firstOrNull()

        return if (weatherDay != null) {
            Pair(weatherDay.minTemperature, weatherDay.maxTemperature)
        } else {
            return Pair(null,null)
        }
    }
    catch (e: Exception) {
        // Handle the exception
        Log.e("WeatherAPI", "Error fetching weather data: ${e.message}", e)
        return Pair(null,null)
        //return null
    }
}




//object RetrofitClient {
//    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
//
//    private val httpClient = OkHttpClient.Builder()
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
//        .build()
//
//    val instance: WeatherApiService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(WeatherApiService::class.java)
//    }
//}
//suspend fun fetchWeatherForDate(location: String, date: String, apiKey: String): WeatherData? {
//    val response = RetrofitClient.instance.getWeather(location, apiKey)
//    val weatherData = response.list.find { it.dt_txt.startsWith(date) }
//
//    Log.d("MainActivity", "Item: $response")
//    return weatherData
//}
//
//suspend fun fetchMinMaxTemperature(location: String, date: String, apiKey: String): Pair< Double?, Double?>? {
//    val weatherData = fetchWeatherForDate(location, date, apiKey)
//    if(weatherData!=null) {
//        val minTemp = weatherData.main.temp_min
//        val maxTemp = weatherData.main.temp_max
//        return Pair( minTemp, maxTemp)
//    }
//    return Pair( null, null)
//}

