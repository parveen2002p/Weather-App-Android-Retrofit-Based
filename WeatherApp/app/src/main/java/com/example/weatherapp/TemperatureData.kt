package com.example.weatherapp
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "temperature_data")
data class TemperatureData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val day: Int,
    val month: Int,
    val year: Int,
    val maxTemp: Double,
    val minTemp: Double
)

data class AvgTemperature(
    //val year: Int,
    val avgMaxTemp: Double,
    val avgMinTemp: Double
)

data class WeatherResponse2(
    @SerializedName("days")
    val days: List<WeatherDay>
)

data class WeatherDay(
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("tempmax")
    val maxTemperature: Double,
    @SerializedName("tempmin")
    val minTemperature: Double,
    @SerializedName("temp")
    val temperature: Double
)