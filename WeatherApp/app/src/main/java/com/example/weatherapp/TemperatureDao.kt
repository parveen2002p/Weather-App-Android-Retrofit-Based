package com.example.weatherapp
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TemperatureDao {
    @Insert
    suspend fun insert(temperatureData: TemperatureData)

    @Query("SELECT * FROM temperature_data WHERE day = :day AND month = :month AND year = :year")
    suspend fun getTemperatureForDate(day: Int, month: Int, year: Int): TemperatureData?

    @Query("SELECT * FROM temperature_data WHERE day = :day AND month = :month")
    suspend fun getTemperatureForDayMonth(day: Int, month: Int): List<TemperatureData>
//
//    @Query("SELECT AVG(maxTemp) as avgMaxTemp, AVG(minTemp) as avgMinTemp FROM temperature_data WHERE day = :day AND month = :month GROUP BY year ORDER BY year DESC LIMIT 10")
//    suspend fun getAverageTemperatureForLast10Years(day: Int, month: Int): List<AvgTemperature>

    @Query("SELECT * FROM temperature_data WHERE day = :day AND month = :month GROUP BY year ORDER BY year DESC LIMIT 10")
    suspend fun getAverageTemperatureForLast10Years(day: Int, month: Int): List<TemperatureData>

//    @Query("SELECT CASE WHEN COUNT(DISTINCT year) >= 10 THEN AVG(maxTemp) ELSE NULL END as avgMaxTemp, " +
//            "CASE WHEN COUNT(DISTINCT year) >= 10 THEN AVG(minTemp) ELSE NULL END as avgMinTemp " +
//            "FROM temperature_data WHERE day = :day AND month = :month GROUP BY year ORDER BY year DESC LIMIT 10")
//    suspend fun getAverageTemperatureForLast10Years(day: Int, month: Int): List<AvgTemperature>


    @Query("SELECT EXISTS(SELECT 1 FROM temperature_data WHERE day = :day AND month = :month AND year = :year LIMIT 1)")
    suspend fun checkExists(day: Int, month: Int, year: Int): Boolean
}

