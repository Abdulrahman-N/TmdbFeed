package com.example.interviewtask

import android.app.Application
import coil.Coil
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject
    lateinit var coilLoader: ImageLoader

    override fun onCreate() {

        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Coil.setImageLoader(coilLoader)

    }
}