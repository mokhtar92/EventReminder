package com.domain.event_reminder.data.remote.calender

import android.content.Intent
import android.net.Uri
import android.os.Environment
import com.domain.event_reminder.App
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Events
import io.reactivex.Single
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.util.*


val calenderService by lazy { CalenderService() }

class CalenderService {
    private val APPLICATION_NAME = "Event Reminder"
    private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
    private val TOKENS_DIRECTORY_PATH = "tokens"
    private val SCOPES = Collections.singletonList(CalendarScopes.CALENDAR)
    private val CREDENTIALS_FILE_PATH = "credentials.json"

    @Throws(IOException::class)
    fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Single<Credential> {
        // Load client secrets.
        val inputStream = App.appContext.assets.open(CREDENTIALS_FILE_PATH)

        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(inputStream))

        val tokenFolder =
            File("${Environment.getExternalStorageDirectory().path}${File.separator}$TOKENS_DIRECTORY_PATH")
        if (!tokenFolder.exists()) tokenFolder.mkdirs()

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
        )
            .setDataStoreFactory(FileDataStoreFactory(tokenFolder))
            .setAccessType("offline")
            .build()

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()

        val authorizationCode = object : AuthorizationCodeInstalledApp(flow, receiver) {
            override fun onAuthorization(authorizationUrl: AuthorizationCodeRequestUrl?) {
                val url = (authorizationUrl?.build())
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                App.appContext.startActivity(browserIntent)
            }
        }

        return Single.create { emitter ->
            emitter.onSuccess(
                authorizationCode.authorize("user")
            )
        }
    }

    @Throws(IOException::class, GeneralSecurityException::class)
    fun getEventsFromCalender(
        calenderId: String,
        orderBy: String,
        includeEmail: Boolean,
        singleEvents: Boolean,
        credential: Credential,
        httpTransport: HttpTransport
    ): Single<Events> {
        // Build a new authorized API client service.
        val service = Calendar.Builder(httpTransport, JSON_FACTORY, credential)
            .setApplicationName(APPLICATION_NAME)
            .build()

        val now = DateTime(System.currentTimeMillis())
        val afterWeek = DateTime(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000))

        return Single.create { emitter ->
            emitter.onSuccess(
                service.events().list(calenderId)
                    .setTimeMin(now)
                    .setTimeMax(afterWeek)
                    .setOrderBy(orderBy)
                    .setAlwaysIncludeEmail(includeEmail)
                    .setSingleEvents(singleEvents)
                    .execute()
            )
        }
    }
}