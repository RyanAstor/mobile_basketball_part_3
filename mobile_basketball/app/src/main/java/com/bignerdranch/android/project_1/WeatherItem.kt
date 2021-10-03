package com.bignerdranch.android.project_1

import com.google.gson.annotations.SerializedName

data class WeatherItem(
    @SerializedName("name") var city: String = ""//,
    //var temp: Double = 0.0
)