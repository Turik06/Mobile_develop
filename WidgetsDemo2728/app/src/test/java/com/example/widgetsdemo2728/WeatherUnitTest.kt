package com.example.widgetsdemo2728

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherUnitTest {

    @Test
    fun testCelsiusToFahrenheitConversion() {
        val weather = WeatherInfo(
            city = "Иркутск",
            temperatureC = 0.0,
            description = "ясно",
            windSpeed = 2.0,
            windDeg = 0,
            source = "Тест"
        )
        // 0 C = 32 F
        assertEquals(32.0, weather.temperatureF, 0.01)

        val weather2 = WeatherInfo(
            city = "Москва",
            temperatureC = 20.0,
            description = "тепло",
            windSpeed = 3.0,
            windDeg = 90,
            source = "Тест"
        )
        // 20 C = 20 * 1.8 + 32 = 68 F
        assertEquals(68.0, weather2.temperatureF, 0.01)
    }

    @Test
    fun testFormattedTemperature() {
        val weatherPos = WeatherInfo("Сочи", 15.6, "солнечно", 1.0, 180, "Тест")
        assertEquals("+16 °C", weatherPos.formattedTemperature(isCelsius = true))
        // 15.6 * 1.8 + 32 = 60.08 -> +60 °F
        assertEquals("+60 °F", weatherPos.formattedTemperature(isCelsius = false))

        val weatherNeg = WeatherInfo("Иркутск", -5.4, "мороз", 2.0, 315, "Тест")
        assertEquals("-5 °C", weatherNeg.formattedTemperature(isCelsius = true))
        // -5.4 * 1.8 + 32 = 22.28 -> +22 °F
        assertEquals("+22 °F", weatherNeg.formattedTemperature(isCelsius = false))
    }

    @Test
    fun testWindDirectionCalculation() {
        fun makeWeather(deg: Int) = WeatherInfo("Тест", 0.0, "ясно", 1.0, deg, "Тест")

        assertTrue(makeWeather(0).windDirectionText.startsWith("Северный (С)"))
        assertTrue(makeWeather(360).windDirectionText.startsWith("Северный (С)"))
        assertTrue(makeWeather(45).windDirectionText.startsWith("Северо-восточный (СВ)"))
        assertTrue(makeWeather(90).windDirectionText.startsWith("Восточный (В)"))
        assertTrue(makeWeather(135).windDirectionText.startsWith("Юго-восточный (ЮВ)"))
        assertTrue(makeWeather(180).windDirectionText.startsWith("Южный (Ю)"))
        assertTrue(makeWeather(225).windDirectionText.startsWith("Юго-западный (ЮЗ)"))
        assertTrue(makeWeather(270).windDirectionText.startsWith("Западный (З)"))
        assertTrue(makeWeather(315).windDirectionText.startsWith("Северо-западный (СЗ)"))
    }
}
