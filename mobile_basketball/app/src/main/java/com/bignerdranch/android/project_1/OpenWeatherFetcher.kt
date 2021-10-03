package com.bignerdranch.android.project_1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.project_1.api.OpenWeatherApi
import com.bignerdranch.android.project_1.api.OpenWeatherResponse
import com.bignerdranch.android.project_1.api.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "OpenWeatherFetcher"

class OpenWeatherFetcher {
    private val openWeatherApi: OpenWeatherApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        openWeatherApi = retrofit.create(OpenWeatherApi::class.java)
    }

    fun fetchWeather(): LiveData<WeatherItem> {
        val responseLiveData: MutableLiveData<WeatherItem> = MutableLiveData()
        val openWeatherRequest: Call<OpenWeatherResponse> = openWeatherApi.fetchWeather()
        openWeatherRequest.enqueue(object : Callback<OpenWeatherResponse> {
            override fun onFailure(call: Call<OpenWeatherResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch current weather", t)
            }
            override fun onResponse(
                call: Call<OpenWeatherResponse>,
                response: Response<OpenWeatherResponse>
            ) {
                Log.d(TAG, "Response received")
                val openWeatherResponse: OpenWeatherResponse? = response.body()
                val weatherResponse: WeatherResponse? = openWeatherResponse?.weather
                val weatherItem: WeatherItem? = weatherResponse?.weatherItem
                responseLiveData.value = weatherItem
            }
        })
        return responseLiveData
    }
}