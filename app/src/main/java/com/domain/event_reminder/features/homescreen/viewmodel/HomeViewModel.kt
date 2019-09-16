package com.domain.event_reminder.features.homescreen.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domain.event_reminder.data.IRepository
import com.domain.event_reminder.data.entities.AppEvent
import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.calendar.model.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat

class HomeViewModel(private val repository: IRepository) : ViewModel() {

    private val disposables by lazy { CompositeDisposable() }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _events = MutableLiveData<List<AppEvent>>()
    val events: LiveData<List<AppEvent>> = _events


    fun getCredentials() {
        _loading.postValue(true)
        val netHttpTransport = NetHttpTransport()
        disposables.add(
            repository.getCredentialsForUser(netHttpTransport)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        getEvents(it, netHttpTransport)

                    }, onError = {
                        _loading.postValue(false)
                        _error.postValue(it.message)
                    }
                )
        )
    }

    private fun getEvents(
        credential: Credential,
        netHttpTransport: NetHttpTransport
    ) {
        disposables.add(
            repository.getCalenderEvents(credential = credential, netHttpTransport = netHttpTransport)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.items }
                .toObservable()
                .concatMap { eventList -> Observable.fromIterable(eventList) }
                .concatMap { event -> getAppEventFromEvent(event) }
                .toList()
                .subscribeBy(
                    onSuccess = {
                        _loading.postValue(false)
                        _events.postValue(it)
                    },
                    onError = {
                        _loading.postValue(false)
                        _error.postValue(it.message)
                    }
                )
        )
    }

    private fun getAppEventFromEvent(event: Event): Observable<AppEvent> {
        return repository.getWeatherData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .map {
                val (date, time) = getDateAndTime(event)

                AppEvent(
                    id = event.id ?: "N/A",
                    title = event.summary ?: "N/A",
                    description = event.description ?: "N/A",
                    date = date,
                    time = time,
                    status = event.status ?: "N/A",
                    temperature = it.main?.temp ?: 0.0,
                    humidity = it.main?.humidity ?: 0.0
                )
            }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateAndTime(event: Event): Pair<String, String> {
        val eventDateTime = event.start.dateTime?.toStringRfc3339()!!

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSX")
        val dateTime = format.parse(eventDateTime)!!

        val dateFormatter = SimpleDateFormat("E, dd MMM yyyy")
        val timeFormatter = SimpleDateFormat("hh:mm a")

        return Pair(dateFormatter.format(dateTime), timeFormatter.format(dateTime))
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}