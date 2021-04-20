package com.paisleydavis.transcribe

import android.app.Application

class TranscribeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }
}