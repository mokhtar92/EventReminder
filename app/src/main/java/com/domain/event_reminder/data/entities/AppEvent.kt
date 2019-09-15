package com.domain.event_reminder.data.entities

data class AppEvent(
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val temperature: String,
    val humidity: String
)