package com.example.widgetsdemo2728

data class WeatherInfo(
    val city: String,
    val temperatureC: Double,
    val description: String,
    val windSpeed: Double,
    val windDeg: Int,
    val source: String
) {
    val temperatureF: Double
        get() = temperatureC * 1.8 + 32.0

    val windDirectionText: String
        get() {
            val normalizedDeg = ((windDeg % 360) + 360) % 360
            val directionName = when (((normalizedDeg + 22.5) / 45.0).toInt() % 8) {
                0 -> "Северный (С)"
                1 -> "Северо-восточный (СВ)"
                2 -> "Восточный (В)"
                3 -> "Юго-восточный (ЮВ)"
                4 -> "Южный (Ю)"
                5 -> "Юго-западный (ЮЗ)"
                6 -> "Западный (З)"
                7 -> "Северо-западный (СЗ)"
                else -> "Северный (С)"
            }
            return "$directionName, $normalizedDeg°"
        }

    fun formattedTemperature(isCelsius: Boolean): String {
        return if (isCelsius) {
            val sign = if (temperatureC > 0) "+" else ""
            "$sign${Math.round(temperatureC)} °C"
        } else {
            val sign = if (temperatureF > 0) "+" else ""
            "$sign${Math.round(temperatureF)} °F"
        }
    }
}
