package com.domain.event_reminder.data.remote.weather

import com.domain.event_reminder.data.entities.WeatherResponse
import com.domain.event_reminder.data.remote.RemoteServiceFactory
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

val weatherService by lazy { WeatherService() }

class WeatherService {

    private val service by lazy { RemoteServiceFactory.getInstance().create(WeatherServiceEndPoint::class.java) }

    fun getWeatherForToday(
        id: String,
        units: String,
        apiKey: String
    ): Single<WeatherResponse> {
        return service.getWeatherForToday(id, units, apiKey)
    }
}

interface WeatherServiceEndPoint {
    @GET("weather")
    fun getWeatherForToday(
        @Query("id") id: String,
        @Query("units") units: String,
        @Query("APPID") apiKey: String
    ): Single<WeatherResponse>
}