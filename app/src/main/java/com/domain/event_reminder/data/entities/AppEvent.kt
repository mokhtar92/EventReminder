package com.domain.event_reminder.data.entities

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.domain.event_reminder.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AppEvent(
    val id: String?,
    val title: String?,
    val description: String?,
    val date: String?,
    val time: String?,
    val status: String?,
    val temperature: Double?,
    val humidity: Double?,
    @DrawableRes var resourceId: Int = R.drawable.ic_sunny
) : Parcelable