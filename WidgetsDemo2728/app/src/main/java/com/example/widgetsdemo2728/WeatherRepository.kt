package com.example.widgetsdemo2728

import android.content.Context
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object WeatherRepository {

    suspend fun loadWeatherFromApi(city: String, apiKey: String): WeatherInfo = withContext(Dispatchers.IO) {
        val query = URLEncoder.encode(city, "UTF-8")
        val key = URLEncoder.encode(apiKey, "UTF-8")
        val url = URL("https://api.openweathermap.org/data/2.5/weather?q=$query&appid=$key&units=metric&lang=ru")
        val connection = url.openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 7000
            connection.readTimeout = 7000
            val code = connection.responseCode
            if (code != HttpURLConnection.HTTP_OK) {
                throw Exception("HTTP error code: $code")
            }
            val text = connection.inputStream.bufferedReader().use { it.readText() }
            val root = JsonParser.parseString(text).asJsonObject
            val main = root.getAsJsonObject("main")
            val weatherArray = root.getAsJsonArray("weather")
            val wind = root.getAsJsonObject("wind")

            val temp = main["temp"].asDouble
            val desc = if (weatherArray != null && weatherArray.size() > 0) {
                weatherArray[0].asJsonObject["description"].asString
            } else "нет данных"
            val windSpeed = wind?.get("speed")?.asDouble ?: 0.0
            val windDeg = wind?.get("deg")?.asInt ?: 0

            WeatherInfo(
                city = city,
                temperatureC = temp,
                description = desc.replaceFirstChar { it.uppercase() },
                windSpeed = windSpeed,
                windDeg = windDeg,
                source = "OpenWeatherMap API"
            )
        } finally {
            connection.disconnect()
        }
    }

    fun loadWeatherFromJson(context: Context, city: String): WeatherInfo {
        context.assets.open("weather.json").use { inputStream ->
            val jsonText = InputStreamReader(inputStream).readText()
            val root = JsonParser.parseString(jsonText).asJsonObject
            if (root.has(city)) {
                val cityObj = root.getAsJsonObject(city)
                val temp = cityObj["temperature"].asDouble
                val desc = cityObj["description"].asString
                val windSpeed = cityObj["windSpeed"].asDouble
                val windDeg = cityObj["windDeg"].asInt
                return WeatherInfo(
                    city = city,
                    temperatureC = temp,
                    description = desc.replaceFirstChar { it.uppercase() },
                    windSpeed = windSpeed,
                    windDeg = windDeg,
                    source = "Локальный JSON"
                )
            }
        }
        return WeatherInfo(
            city = city,
            temperatureC = 0.0,
            description = "Данные отсутствуют",
            windSpeed = 0.0,
            windDeg = 0,
            source = "Локальный JSON"
        )
    }

    suspend fun loadWeather(context: Context, city: String, apiKey: String): WeatherInfo {
        return try {
            if (apiKey.isNotBlank()) {
                loadWeatherFromApi(city, apiKey)
            } else {
                loadWeatherFromJson(context, city)
            }
        } catch (e: Exception) {
            android.util.Log.e("WeatherApp", "Failed to load from API: ${e.message}", e)
            // Если сеть недоступна, возвращаем данные из локального JSON файла
            loadWeatherFromJson(context, city)
        }
    }
}
