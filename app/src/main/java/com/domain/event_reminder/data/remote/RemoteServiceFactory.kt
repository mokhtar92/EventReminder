package com.domain.event_reminder.data.remote

import com.domain.event_reminder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class RemoteServiceFactory private constructor() {

    companion object {
        const val WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/"

        @Volatile
        private var instance: Retrofit? = null

        fun getInstance() = instance ?: synchronized(this) {
            makeRetrofitClient().also { instance = it }
        }

        private fun makeRetrofitClient(): Retrofit = Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .client(makeOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()


        private fun makeOkHttpClient(): OkHttpClient {
            val httpClientBuilder = OkHttpClient.Builder()

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpClientBuilder.addInterceptor(loggingInterceptor)
            }

            return httpClientBuilder.build()
        }
    }
}
