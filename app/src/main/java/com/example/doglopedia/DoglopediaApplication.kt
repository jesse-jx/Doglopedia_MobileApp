package com.example.doglopedia

import android.app.Application
import com.example.doglopedia.database.AppContainer
import com.example.doglopedia.database.DefaultAppContainer

class DoglopediaApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}