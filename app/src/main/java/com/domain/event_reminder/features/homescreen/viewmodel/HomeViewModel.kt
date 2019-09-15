package com.domain.event_reminder.features.homescreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domain.event_reminder.data.repository
import com.google.api.services.calendar.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private val disposables by lazy { CompositeDisposable() }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events


    fun getEvents() {
        _loading.postValue(true)
        disposables.add(
            repository.getCalenderEvents()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        _loading.postValue(false)
                        _events.postValue(it.items)
                    },
                    onError = {
                        _loading.postValue(false)
                        _error.postValue(it.message)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}