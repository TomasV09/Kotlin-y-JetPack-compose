package com.example.compose3

enum class WeatherCondition {
    SUNNY, RAINY, NIGHT
}

data class WeatherData(
    val city: String,
    val temperature: Int,
    val description: String,
    val humidity: String,
    val wind: String,
    val condition: WeatherCondition
)
