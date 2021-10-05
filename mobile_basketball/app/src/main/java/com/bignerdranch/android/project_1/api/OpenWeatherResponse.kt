package com.bignerdranch.android.project_1.api

import com.google.gson.annotations.SerializedName

class OpenWeatherResponse {
    var main: Main? = null
    @SerializedName("name")
    var city: String? = null
}

class Main {
    var temp: Float = 0.toFloat()
}