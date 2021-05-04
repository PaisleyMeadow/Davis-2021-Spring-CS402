package com.paisleydavis.transcribe
import android.content.Context
import android.util.Log
import com.paisleydavis.transcribe.dataClasses.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser

object ObjectBox {
    lateinit var boxStore: BoxStore
    private set

    fun init(context: Context){
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()

        // initialize objectbox browser database viewer for debugging
        if(BuildConfig.DEBUG){
            AndroidObjectBrowser(boxStore).start(context.applicationContext)
        }
    }
}
