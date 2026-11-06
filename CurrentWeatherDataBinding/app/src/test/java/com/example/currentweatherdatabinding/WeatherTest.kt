package com.example.currentweatherdatabinding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class WeatherTest {
    private val response = """
        {"name":"Иркутск","weather":[{"description":"ясно"}],"main":{"temp":-12.5}}
    """.trimIndent()

    @Test
    fun parsesWeather() {
        val weather = Weather.fromJson(response)
        assertEquals("Иркутск", weather.city)
        assertEquals("ясно", weather.description)
        assertEquals(-12.5, weather.temperature, 0.001)
    }

    @Test
    fun rejectsInvalidResponse() {
        for (json in listOf("", "{}", response.replace("-12.5", "\"NaN\""))) {
            assertThrows(Exception::class.java) { Weather.fromJson(json) }
        }
    }
}
