package com.domain.event_reminder.data.entities

import com.squareup.moshi.Json

data class WeatherResponse(
    @field:Json(name = "coord") var coord: Coord?,
    @field:Json(name = "weather") var weather: List<Weather>?,
    @field:Json(name = "base") var base: String?,
    @field:Json(name = "main") var main: Main?,
    @field:Json(name = "visibility") var visibility: Int?,
    @field:Json(name = "wind") var wind: Wind?,
    @field:Json(name = "clouds") var clouds: Clouds?,
    @field:Json(name = "dt") var dt: Int?,
    @field:Json(name = "sys") var sys: Sys?,
    @field:Json(name = "timezone") var timezone: Int?,
    @field:Json(name = "id") var id: Int?,
    @field:Json(name = "name") var name: String?,
    @field:Json(name = "cod") var cod: Int?
)

data class Coord(
    @field:Json(name = "lon") var lon: Double?,
    @field:Json(name = "lat") var lat: Double?
)

data class Weather(
    @field:Json(name = "id") var id: Int?,
    @field:Json(name = "main") var main: String?,
    @field:Json(name = "description") var description: String?,
    @field:Json(name = "icon") var icon: String?
)

data class Main(
    @field:Json(name = "temp") var temp: Double?,
    @field:Json(name = "pressure") var pressure: Int?,
    @field:Json(name = "humidity") var humidity: Int?,
    @field:Json(name = "temp_min") var tempMin: Int?,
    @field:Json(name = "temp_max") var tempMax: Int?
)

data class Wind(
    @field:Json(name = "speed") var speed: Double?,
    @field:Json(name = "deg") var deg: Int?
)

data class Clouds(
    @field:Json(name = "all") var all: Int?
)

data class Sys(
    @field:Json(name = "type") var type: Int?,
    @field:Json(name = "id") var id: Int?,
    @field:Json(name = "message") var message: Double?,
    @field:Json(name = "country") var country: String?,
    @field:Json(name = "sunrise") var sunrise: Int?,
    @field:Json(name = "sunset") var sunset: Int?
)
