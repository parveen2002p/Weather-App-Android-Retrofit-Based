package com.example.weatherapp
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TemperatureRepository(private val temperatureDao : TemperatureDao) {

    suspend fun insertTemperatureData(temperatureData: TemperatureData) {
        withContext(Dispatchers.IO) {
            temperatureDao.insert(temperatureData)
        }
    }

    suspend fun hasTemperatureDataForDate(day: Int, month: Int, year: Int) :Boolean{
        return withContext(Dispatchers.IO) {
            temperatureDao.checkExists(day, month, year)
        }
    }

    suspend fun getTemperatureForDate(day: Int, month: Int, year: Int): TemperatureData? {
        return withContext(Dispatchers.IO) {
            temperatureDao.getTemperatureForDate(day, month, year)
        }
    }

    suspend fun getTemperatureForDayMonth(day: Int, month: Int): List<TemperatureData> {
        return withContext(Dispatchers.IO) {
            temperatureDao.getTemperatureForDayMonth(day, month)
        }
    }

    suspend fun getAverageTemperatureForLast10Years(day: Int, month: Int): List<TemperatureData> {
        return withContext(Dispatchers.IO) {
            temperatureDao.getAverageTemperatureForLast10Years(day, month)
        }
    }
}
