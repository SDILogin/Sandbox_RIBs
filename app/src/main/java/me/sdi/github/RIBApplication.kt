package me.sdi.github

import android.app.Application
import timber.log.Timber

class RIBApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}