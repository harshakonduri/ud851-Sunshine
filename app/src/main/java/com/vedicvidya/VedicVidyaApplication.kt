package com.vedicvidya

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for Vedic Vidya
 * Initializes Hilt for dependency injection
 */
@HiltAndroidApp
class VedicVidyaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Application-level initialization
    }
}
