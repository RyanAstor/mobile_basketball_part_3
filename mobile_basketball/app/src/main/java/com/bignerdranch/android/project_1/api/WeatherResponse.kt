package com.bignerdranch.android.project_1.api

import com.bignerdranch.android.project_1.WeatherItem
import com.google.gson.annotations.SerializedName

class WeatherResponse {
//    @SerializedName("weather")
    lateinit var weatherItem: WeatherItem
}