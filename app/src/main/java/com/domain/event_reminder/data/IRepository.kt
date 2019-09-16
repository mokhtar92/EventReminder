package com.domain.event_reminder.data

import com.domain.event_reminder.data.entities.WeatherResponse
import com.domain.event_reminder.utils.WEATHER_API_KEY
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.calendar.model.Events
import io.reactivex.Single

interface IRepository {

    fun getCredentialsForUser(netHttpTransport: NetHttpTransport): Single<Credential>

    fun getCalenderEvents(
        calenderId: String = "primary",
        orderBy: String = "startTime",
        includeEmail: Boolean = true,
        singleEvents: Boolean = true,
        credential: Credential,
        netHttpTransport: NetHttpTransport
    ): Single<Events>

    fun getWeatherData(
        id: String = "360630",
        units: String = "metric",
        apiKey: String = WEATHER_API_KEY
    ): Single<WeatherResponse>
}