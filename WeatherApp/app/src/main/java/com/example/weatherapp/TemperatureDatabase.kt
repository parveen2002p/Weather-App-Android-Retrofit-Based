package com.example.weatherapp
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TemperatureData::class], version = 1)
abstract class TemperatureDatabase : RoomDatabase() {
    abstract fun temperatureDao(): TemperatureDao
}
