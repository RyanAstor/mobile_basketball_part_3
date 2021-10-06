package com.bignerdranch.android.project_1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.project_1.api.OpenWeatherApi
import com.bignerdranch.android.project_1.api.OpenWeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    fun fetchWeather(lat: Double, lon: Double): LiveData<WeatherItem> {
        val responseLiveData: MutableLiveData<WeatherItem> = MutableLiveData()
        val openWeatherRequest: Call<OpenWeatherResponse> = openWeatherApi.fetchWeather(
            lat,lon,"1dc6c314be1cb96dfc14e8050c6")
        Log.d(TAG, "$lat, $lon")
        openWeatherRequest.enqueue(object : Callback<OpenWeatherResponse> {
            override fun onFailure(call: Call<OpenWeatherResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch current weather", t)
            }
            override fun onResponse(
                call: Call<OpenWeatherResponse>,
                response: Response<OpenWeatherResponse>
            ) {
                Log.d(TAG, "Response received")
                val openWeatherResponse = response.body()
                Log.d(TAG, openWeatherResponse.toString())
                val weatherItem = openWeatherResponse?.city?.let { city ->
                    openWeatherResponse.main?.temp?.let { temp ->
                        WeatherItem(city, temp.toDouble())
                    }
                }
                responseLiveData.value = weatherItem
            }
        })
        return responseLiveData
    }
}