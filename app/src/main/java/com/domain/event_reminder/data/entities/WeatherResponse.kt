package com.domain.event_reminder.data.entities

import com.squareup.moshi.Json

data class WeatherResponse(
    @field:Json(name = "coord") var coord: Coord?,
    @field:Json(name = "weather") var weather: List<Weather>?,
    @field:Json(name = "base") var base: String?,
    @field:Json(name = "main") var main: Main?,
    @field:Json(name = "visibility") var visibility: Double?,
    @field:Json(name = "wind") var wind: Wind?,
    @field:Json(name = "clouds") var clouds: Clouds?,
    @field:Json(name = "dt") var dt: Double?,
    @field:Json(name = "sys") var sys: Sys?,
    @field:Json(name = "timezone") var timezone: Double?,
    @field:Json(name = "id") var id: Double?,
    @field:Json(name = "name") var name: String?,
    @field:Json(name = "cod") var cod: Double?
)

data class Coord(
    @field:Json(name = "lon") var lon: Double?,
    @field:Json(name = "lat") var lat: Double?
)

data class Weather(
    @field:Json(name = "id") var id: Double?,
    @field:Json(name = "main") var main: String?,
    @field:Json(name = "description") var description: String?,
    @field:Json(name = "icon") var icon: String?
)

data class Main(
    @field:Json(name = "temp") var temp: Double?,
    @field:Json(name = "pressure") var pressure: Double?,
    @field:Json(name = "humidity") var humidity: Double?,
    @field:Json(name = "temp_min") var tempMin: Double?,
    @field:Json(name = "temp_max") var tempMax: Double?
)

data class Wind(
    @field:Json(name = "speed") var speed: Double?,
    @field:Json(name = "deg") var deg: Double?
)

data class Clouds(
    @field:Json(name = "all") var all: Double?
)

data class Sys(
    @field:Json(name = "type") var type: Double?,
    @field:Json(name = "id") var id: Double?,
    @field:Json(name = "message") var message: Double?,
    @field:Json(name = "country") var country: String?,
    @field:Json(name = "sunrise") var sunrise: Double?,
    @field:Json(name = "sunset") var sunset: Double?
)
