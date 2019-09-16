package com.domain.event_reminder.data.entities

data class AppEvent(
    val id: String?,
    val title: String?,
    val description: String?,
    val date: String?,
    val time: String?,
    val status: String?,
    val temperature: Double?,
    val humidity: Double?
)