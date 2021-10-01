package com.bignerdranch.android.project_1

import android.app.Application

class BasketballIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(this)
    }
}