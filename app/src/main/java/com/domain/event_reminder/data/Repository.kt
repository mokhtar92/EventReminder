package com.domain.event_reminder.data

import com.domain.event_reminder.data.entities.WeatherResponse
import com.domain.event_reminder.data.remote.calender.CalenderService
import com.domain.event_reminder.data.remote.calender.calenderService
import com.domain.event_reminder.data.remote.weather.WeatherService
import com.domain.event_reminder.data.remote.weather.weatherService
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.calendar.model.Events
import io.reactivex.Single

val repository: IRepository by lazy { Repository() }

class Repository(
    private val calender: CalenderService = calenderService,
    private val weather: WeatherService = weatherService
) : IRepository {
    override fun getCredentialsForUser(netHttpTransport: NetHttpTransport): Single<Credential> {
        return calender.getCredentials(netHttpTransport)
    }

    override fun getCalenderEvents(
        calenderId: String,
        orderBy: String,
        includeEmail: Boolean,
        singleEvents: Boolean,
        credential: Credential,
        netHttpTransport: NetHttpTransport
    ): Single<Events> {
        return calender.getEventsFromCalender(
            calenderId,
            orderBy,
            includeEmail,
            singleEvents,
            credential,
            netHttpTransport
        )
    }

    override fun getWeatherData(id: String, units: String, apiKey: String): Single<WeatherResponse> {
        return weather.getWeatherForToday(id, units, apiKey)
    }
}