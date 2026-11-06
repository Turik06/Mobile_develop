package com.example.currentweatherdatabinding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class WeatherHttpException(val statusCode: Int) : Exception()

object WeatherApi {
    suspend fun loadWeather(city: String, apiKey: String): Weather = withContext(Dispatchers.IO) {
        val query = URLEncoder.encode(city, "UTF-8")
        val key = URLEncoder.encode(apiKey, "UTF-8")
        val connection = URL(
            "https://api.openweathermap.org/data/2.5/weather?q=$query&appid=$key&units=metric&lang=ru"
        ).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            val status = connection.responseCode
            if (status != HttpURLConnection.HTTP_OK) throw WeatherHttpException(status)
            connection.inputStream.bufferedReader(Charsets.UTF_8).use { Weather.fromJson(it.readText()) }
        } finally {
            connection.disconnect()
        }
    }
}
