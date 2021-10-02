package com.bignerdranch.android.project_1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.project_1.api.OpenWeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "OpenWeatherFetcher"

class OpenWeatherFetcher {
    private val openWeatherApi: OpenWeatherApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        openWeatherApi = retrofit.create(OpenWeatherApi::class.java)
    }

    fun fetchWeather(): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val openWeatherRequest: Call<String> = openWeatherApi.fetchWeather()
        openWeatherRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch current weather", t)
            }
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "Response received")
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }
}